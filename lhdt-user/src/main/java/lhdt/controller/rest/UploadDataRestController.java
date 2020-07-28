package lhdt.controller.rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import dev.hyunlab.core.util.PpUtil;
import lhdt.LhdtUserApplication;
import lhdt.config.PropertiesConfig;
import lhdt.controller.LhdtAbstractController;
import lhdt.domain.CacheManager;
import lhdt.domain.FileType;
import lhdt.domain.Key;
import lhdt.domain.Policy;
import lhdt.domain.UploadData;
import lhdt.domain.UploadDataFile;
import lhdt.domain.UploadDataType;
import lhdt.domain.UploadDirectoryType;
import lhdt.domain.UserSession;
import lhdt.service.UploadDataService;
import lhdt.utils.DateUtils;
import lhdt.utils.FileUtils;
import lhdt.utils.FormatUtils;
import lhdt.utils.LhdtConst;
import lhdt.utils.LhdtUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 3D 데이터 파일 업로더
 * TODO 설계 파일 안의 texture 의 경우 설계 파일에서 참조하는 경우가 있으므로 이름 변경 불가.
 * @author jeongdae
 *
 */
@Slf4j
@RestController
@RequestMapping("/upload-datas")
public class UploadDataRestController extends LhdtAbstractController{
	
	// 파일 copy 시 버퍼 사이즈
	public static final int BUFFER_SIZE = 8192;
	
	@Autowired
	private PropertiesConfig propertiesConfig;
	
	@Autowired
	private UploadDataService uploadDataService;
	
	/**
	 * TODO 비동기로 처리해야 할듯
	 * data upload 처리
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping
	public Map<String, Object> insert(MultipartHttpServletRequest request) throws Exception {
		Map<String, Object> result = new HashMap<>();
		String errorCode = null;
		String message = null;
		String dataType = request.getParameter("dataType");
		
		// converter 변환 대상 파일 수
		int converterTargetCount = 0;
		
		Policy policy = CacheManager.getPolicy();
		// 여긴 null 체크를 안 하는게 맞음. 없음 장애가 나야 함
		// 업로딩 가능한 파일 타입
		String[] uploadTypes = policy.getUserUploadType().toLowerCase().split(",");
		// 변환 가능한 파일 타입
		String[] converterTypes = policy.getUserConverterType().split(",");
		List<String> uploadTypeList = Arrays.asList(uploadTypes);
		List<String> converterTypeList = Arrays.asList(converterTypes);
		
		errorCode = dataValidate(request);
		if(!StringUtils.isEmpty(errorCode)) {
			return super.createResultMap(HttpStatus.BAD_REQUEST, errorCode, message);
		}

		//
		String userId = super.getUserId(request);
		List<UploadDataFile> uploadDataFileList = new ArrayList<>();
		Map<String, MultipartFile> fileMap = request.getFileMap();
		
		Map<String, Object> uploadMap = null;
		String today = DateUtils.getToday(FormatUtils.YEAR_MONTH_DAY_TIME14);
		
		// 1 directory 생성
		String makedDirectory = FileUtils.makeDirectory(userId, UploadDirectoryType.YEAR_MONTH, propertiesConfig.getDataUploadDir());
		log.info("@@@@@@@ = {}", makedDirectory);
		
		// 2 한건이면서 zip 의 경우
		boolean isZipFile = false;
		int fileCount = fileMap.values().size();
		log.debug("fileCount - {}", fileCount);
		
		//
		if(fileCount == 1) {
			// processAsync(policy, userId, fileMap, makedDirectory);
			for (MultipartFile multipartFile : fileMap.values()) {
				String[] divideNames = multipartFile.getOriginalFilename().split("\\.");
				String fileExtension = divideNames[divideNames.length - 1];
				if(UploadData.ZIP_EXTENSION.equalsIgnoreCase(fileExtension)) {
					
					isZipFile = true;
					// zip 파일
					uploadMap = unzip(policy, uploadTypeList, converterTypeList, today, userId, multipartFile, makedDirectory, dataType);
					log.info("@@@@@@@ uploadMap = {}", uploadMap);
					
					// validation 체크
					if(uploadMap.containsKey("errorCode")) {
						errorCode = (String)uploadMap.get("errorCode");
			            return super.createResultMap(HttpStatus.BAD_REQUEST, errorCode, message);
					}
					
					// converter 변환 대상 파일 수
					converterTargetCount = (Integer)uploadMap.get("converterTargetCount");
					if(converterTargetCount <= 0) {
						log.info("@@@@@@@@@@@@ converterTargetCount = {}", converterTargetCount);
			            return super.createResultMap(HttpStatus.BAD_REQUEST, "converter.target.count.invalid", message);
					}
					
					uploadDataFileList = (List<UploadDataFile>)uploadMap.get("uploadDataFileList");
				}
			}
		}
		
		if(!isZipFile) {
			// zip 파일이 아니면 기본적으로 한 폴더에 넣어야 함
			
			String tempDirectory = userId + "_" + System.nanoTime();
			// 파일을 upload 디렉토리로 복사
			FileUtils.makeDirectory(makedDirectory + tempDirectory);
			// 3 그 외의 경우는 재귀적으로 파일 복사
			for (MultipartFile multipartFile : fileMap.values()) {
				log.info("@@@@@@@@@@@@@@@ name = {}, originalName = {}", multipartFile.getName(), multipartFile.getOriginalFilename());
				
				Boolean converterTarget = false;
				
				// 파일 기본 validation 체크
				errorCode = fileValidate(policy, uploadTypeList, multipartFile);
				if(!StringUtils.isEmpty(errorCode)) {
		            return super.createResultMap(HttpStatus.BAD_REQUEST, errorCode, message);
				}
				
				String originalName = multipartFile.getOriginalFilename();
				String[] divideFileName = originalName.split("\\.");
    			String saveFileName = originalName;
    			
    			// validation
    			if(PpUtil.isEmpty(divideFileName)) {
    				log.info("@@@@@@@@@@@@ upload.file.type.invalid. originalName = {}", originalName);
		            return super.createResultMap(HttpStatus.BAD_REQUEST, "upload.file.type.invalid", message);
    			}
				
    			String extension = divideFileName[divideFileName.length - 1];
    			// !extList.contains(extension.toLowerCase())
				if(UploadData.ZIP_EXTENSION.equalsIgnoreCase(extension) || !uploadTypeList.contains(extension.toLowerCase())) {
					log.info("@@@@@@@@@@@@ upload.file.type.invalid. originalName = {}", originalName);
					return super.createResultMap(HttpStatus.BAD_REQUEST, "upload.file.type.invalid", message);
				}
				
				if(converterTypeList.contains(extension.toLowerCase())) {
					if(!dataType.equalsIgnoreCase(extension)) {
						// 데이터 타입과 업로딩 파일 확장자가 같지 않고
						if(	UploadDataType.CITYGML == UploadDataType.findBy(dataType)
								&& UploadDataType.GML.getValue().equalsIgnoreCase(extension)){
							// 데이터 타입은 citygml 인데 확장자는 gml 인 경우 통과
						} else if(UploadDataType.INDOORGML == UploadDataType.findBy(dataType)
								&& UploadDataType.GML.getValue().equalsIgnoreCase(extension)) {
							// 데이터 타입은 indoorgml 인데 확장자는 gml 인 경우 통과
						} else {
							// 전부 예외
							log.info("@@@@@@@@@@@@ datatype = {}, extension = {}", dataType, extension);
	    					return super.createResultMap(HttpStatus.BAD_REQUEST, "upload.file.type.invalid", message);
						}
					}
					
					if(UploadDataType.CITYGML == UploadDataType.findBy(dataType) && UploadDataType.INDOORGML == UploadDataType.findBy(extension)) {
						// 전부 예외
						log.info("@@@@@@@@@@@@ 데이터 타입이 다른 경우. datatype = {}, extension = {}", dataType, extension);
						result.put("errorCode", "file.ext.invalid");
						return result;
					}
					
					if (UploadDataType.CITYGML.getValue().equalsIgnoreCase(dataType) && UploadDataType.GML.getValue().equalsIgnoreCase(extension)) {
						extension = UploadDataType.CITYGML.getValue();
					} else if (UploadDataType.INDOORGML.getValue().equalsIgnoreCase(dataType) && UploadDataType.GML.getValue().equalsIgnoreCase(extension)) {
						extension = UploadDataType.INDOORGML.getValue();
					}
					// 변환 대상 파일만 이름을 변경하고 나머지 파일은 그대로 이름 유지
					saveFileName = userId + "_" + today + "_" + System.nanoTime() + "." + extension;
					converterTarget = true;
					converterTargetCount++;
				}
				
				//
				log.debug("outputFolder:{}", Paths.get(makedDirectory, tempDirectory, saveFileName));
    			
				
				//파일 저장
				Map<String,Object> resultMap = copyFile(multipartFile, Paths.get(makedDirectory, tempDirectory, saveFileName));
				if(LhdtUtils.isNotEmpty(resultMap.get(LhdtConst.MESSAGE))) {
					return super.createResultMap(HttpStatus.INTERNAL_SERVER_ERROR, "file.copy.exception", ""+resultMap.get(LhdtConst.MESSAGE));
				}
				
				//파일 메타정보
				UploadDataFile uploadDataFile = UploadDataFile.builder()
						.fileType(FileType.FILE.name())
						.fileExt(extension)
						.fileName(multipartFile.getOriginalFilename())
						.fileRealName(saveFileName)
						.filePath(makedDirectory + tempDirectory + File.separator)
						.fileSubPath(tempDirectory)
						.fileSize(String.valueOf(multipartFile.getSize()))
						.converterTarget(converterTarget)
						.depth(1)
						.build();
				
				//리스트에 추가
				uploadDataFileList.add(uploadDataFile);


			}//files
		}//iszipfile
		
		//
		if(converterTargetCount <= 0) {
			log.info("@@@@@@@@@@@@ converterTargetCount = {}", converterTargetCount);
            return super.createResultMap(HttpStatus.BAD_REQUEST, "converter.target.count.invalid", message);
		}

		UploadData uploadData = UploadData.builder()
				.dataName(request.getParameter("dataName"))
				.dataGroupId(Integer.valueOf(request.getParameter("dataGroupId")))
				.sharing(request.getParameter("sharing"))
				.dataType(dataType)
				.userId(userId)
				.fileCount(uploadDataFileList.size())
				.converterTargetCount(converterTargetCount)
				.description(request.getParameter("description"))
				.build();
		
		
		// citygml 인 경우 converter 에서 자동 추출
		if(	UploadDataType.CITYGML != UploadDataType.findBy(dataType)) {
			uploadData.setLongitude(new BigDecimal(request.getParameter("longitude")) );
			uploadData.setLatitude(new BigDecimal(request.getParameter("latitude")) );
			uploadData.setAltitude(new BigDecimal(request.getParameter("altitude")) );
			uploadData.setLocation("POINT(" + request.getParameter("longitude") + " " + request.getParameter("latitude") + ")");
		}
		
		log.info("@@@@@@@@@@@@ uploadData = {}", uploadData);
		//
		uploadDataService.insertUploadData(uploadData, uploadDataFileList);       
		
		//
		return super.createResultMap(HttpStatus.OK, errorCode, message);
	}
	
	
	
	/**
	 * 파일 복사
	 * @param multipartFile
	 * @param targetPath 파일이 저장될 위치 경로
	 * @return 성공일 경우 빈값. 오류일 경우 오류 메시지
	 */
	private Map<String,Object> copyFile(MultipartFile multipartFile, Path targetPath) {
		try {
			return copyFile(multipartFile.getInputStream(), targetPath);
		} catch (IOException e) {
			Map<String,Object> resultMap = new HashMap<>();
			resultMap.put("size", 0);
			resultMap.put(LhdtConst.MESSAGE, (null != e.getCause().getMessage() ? e.getCause().getMessage() : e.getMessage()));
			return resultMap;
		}
		
	}
	
	
	
	
	/**
	 * 파일 복사
	 * @param inputStream
	 * @param targetPath
	 * @return
	 */
	private Map<String,Object> copyFile(InputStream inputStream, Path targetPath) {
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("size", 0);
		resultMap.put(LhdtConst.MESSAGE, "");
		
		//
		Path onlyPath;
		
		//
		String filename = targetPath.toFile().getName(); 
		if(filename.contains(".")) {
			onlyPath = Paths.get(targetPath.toString().replaceAll(filename, ""));
		}else {
			onlyPath = targetPath;
		}
		
		//
		if(!onlyPath.toFile().exists()) {
			onlyPath.toFile().mkdirs();
		}
		
		//
		try(inputStream; OutputStream outputStream = new FileOutputStream(targetPath.toFile())){
			//
			long size=0L;
			int bytesRead = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((bytesRead = inputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
				//업로드된 파일 서버 특정 경로에 저장
				size += bytesRead;
				outputStream.write(buffer, 0, bytesRead);
			}
			
			//
			resultMap.put("size", size);
			//
			return resultMap;
			
		} catch (Exception e1) {
			log.error("{}", e1);
			resultMap.put(LhdtConst.MESSAGE, null != e1.getCause() ? e1.getCause().getMessage() : e1.getMessage());
			//
			return resultMap;
		}
		
	}
	
	/**
	 * 업로딩 파일을 압축 해제
	 * @param policy
	 * @param uploadTypeList
	 * @param converterTypeList
	 * @param today
	 * @param userId
	 * @param multipartFile
	 * @param targetDirectory
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> unzip(	Policy policy, 
										List<String> uploadTypeList, 
										List<String> converterTypeList, 
										String today, 
										String userId, 
										MultipartFile multipartFile, 
										String targetDirectory,
										String dataType) throws Exception {
		log.info(">>");
		
		Map<String, Object> result = new HashMap<>();
		// converter 변환 대상 파일 수
		int converterTargetCount = 0;
		
		String errorCode = fileValidate(policy, uploadTypeList, multipartFile);
		if(!StringUtils.isEmpty(errorCode)) {
			result.put("errorCode", errorCode);
			log.info("<< {}", result);
			return result;
		}
		
		// input directory 생성
		targetDirectory = targetDirectory + userId + "_" + System.nanoTime() + File.separator;
		FileUtils.makeDirectory(targetDirectory);
		
		File uploadedFile = new File(targetDirectory + multipartFile.getOriginalFilename());
		multipartFile.transferTo(uploadedFile);
		log.debug("uploadedFile - {}", uploadedFile);
		
		//
		List<UploadDataFile> uploadDataFileList = new ArrayList<>();
		// zip 파일을 압축할때 한글이나 다국어가 포함된 경우 java.lang.IllegalArgumentException: malformed input off 같은 오류가 발생. 윈도우가 CP949 인코딩으로 파일명을 저장하기 때문.
		// Charset CP949 = Charset.forName("UTF-8");
//		try ( ZipFile zipFile = new ZipFile(uploadedFile, CP949);) {
		try ( ZipFile zipFile = new ZipFile(uploadedFile);) {
			String directoryPath = targetDirectory;
			String subDirectoryPath = "";
			String directoryName = null;
			int depth = 1;
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			
			while( entries.hasMoreElements() ) {
            	UploadDataFile uploadDataFile = new UploadDataFile();
            	
            	ZipEntry entry = entries.nextElement();
            	log.debug("+.entry - {}", entry);
            	
            	String unzipfileName = targetDirectory + entry.getName();
            	Boolean converterTarget = false;
            	
            	if( entry.isDirectory() ) {
            		// 디렉토리인 경우
            		uploadDataFile.setFileType(FileType.DIRECTORY.name());
            		if(directoryName == null) {
            			uploadDataFile.setFileName(entry.getName());
            			uploadDataFile.setFileRealName(entry.getName());
            			directoryName = entry.getName();
            			directoryPath = directoryPath + directoryName;
            			//subDirectoryPath = directoryName;
            		} else {
            			String fileName = null;
            			if(entry.getName().indexOf(directoryName) >=0) {
            				fileName = entry.getName().substring(entry.getName().indexOf(directoryName) + directoryName.length());  
            			} else {
            				// 이전이 디렉토리, 현재도 디렉토리인데 서로 다른 디렉토리
            				if(directoryPath.indexOf(directoryName) >=0) {
            					directoryPath = directoryPath.replace(directoryName, "");
            					directoryName = null;
            				}
            				fileName = entry.getName();
            			}
            			uploadDataFile.setFileName(fileName);
            			uploadDataFile.setFileRealName(fileName);
            			directoryName = fileName;
            			directoryPath = directoryPath + fileName;
            			subDirectoryPath = fileName;
            		}
            		
                	File file = new File(unzipfileName);
                    file.mkdirs();
                    uploadDataFile.setFilePath(directoryPath);
                    uploadDataFile.setFileSubPath(subDirectoryPath);
                    uploadDataFile.setDepth(depth);
                    depth++;
            	} else {
            		// 파일인 경우
            		String fileName = null;
            		String extension = null;
            		String[] divideFileName = null;
            		String saveFileName = null;
            		
            		// TODO zip 파일도 확장자 validation 체크를 해야 함
            		if(directoryName == null) {
            			fileName = entry.getName();
            			divideFileName = fileName.split("\\.");
            			saveFileName = fileName;
            			if(divideFileName != null && divideFileName.length != 0) {
            				extension = divideFileName[divideFileName.length - 1];
            				if(uploadTypeList.contains(extension.toLowerCase())) {
            					if(converterTypeList.contains(extension.toLowerCase())) {
            						if(!dataType.equalsIgnoreCase(extension)) {
                						// 데이터 타입과 업로딩 파일 확장자가 같지 않고
                						if(	UploadDataType.CITYGML == UploadDataType.findBy(dataType)
                								&& UploadDataType.GML.getValue().equalsIgnoreCase(extension)){
                							// 데이터 타입은 citygml 인데 확장자는 gml 인 경우 통과
                						} else if(UploadDataType.INDOORGML == UploadDataType.findBy(dataType)
                								&& UploadDataType.GML.getValue().equalsIgnoreCase(extension)) {
                							// 데이터 타입은 indoorgml 인데 확장자는 gml 인 경우 통과
                						} else {
                							// 전부 예외
                							log.info("@@@@@@@@@@@@ datatype = {}, extension = {}", dataType, extension);
                							result.put("errorCode", "file.ext.invalid");
                							return result;
                						}
                					}
            						
            						if(UploadDataType.CITYGML == UploadDataType.findBy(dataType) && UploadDataType.INDOORGML == UploadDataType.findBy(extension)) {
            							// 전부 예외
            							log.info("@@@@@@@@@@@@ 데이터 타입이 다른 경우. datatype = {}, extension = {}", dataType, extension);
            							result.put("errorCode", "file.ext.invalid");
            							return result;
            						}
            						
            						if (UploadDataType.CITYGML.getValue().equalsIgnoreCase(dataType) && UploadDataType.GML.getValue().equalsIgnoreCase(extension)) {
                						extension = UploadDataType.CITYGML.getValue();
                					} else if (UploadDataType.INDOORGML.getValue().equalsIgnoreCase(dataType) && UploadDataType.GML.getValue().equalsIgnoreCase(extension)) {
                						extension = UploadDataType.INDOORGML.getValue();
                					}
            						
            						// 변환 대상 파일만 이름을 변경하고 나머지 파일은 그대로 이름 유지
            						saveFileName = userId + "_" + today + "_" + System.nanoTime() + "." + extension;
            						converterTarget = true;
            						converterTargetCount++;
            					}
	        				}
            			}
            		} else {
            			if(entry.getName().indexOf(directoryName) >= 0) {
            				// 디렉토리내 파일의 경우
            				fileName = entry.getName().substring(entry.getName().indexOf(directoryName) + directoryName.length());  
            			} else {
            				fileName = entry.getName();
            				if(directoryPath.indexOf(directoryName) >= 0) {
            					directoryPath = directoryPath.replace(directoryName, "");
            					directoryName = null;
            				}
            			}
            			divideFileName = fileName.split("\\.");
            			saveFileName = fileName;
            			if(divideFileName != null && divideFileName.length != 0) {
            				extension = divideFileName[divideFileName.length - 1];
            				if(uploadTypeList.contains(extension.toLowerCase())) {
            					if(converterTypeList.contains(extension.toLowerCase())) {
            						if(!dataType.equalsIgnoreCase(extension)) {
                						// 데이터 타입과 업로딩 파일 확장자가 같지 않고
                						if(	UploadDataType.CITYGML == UploadDataType.findBy(dataType)
                								&& UploadDataType.GML.getValue().equalsIgnoreCase(extension)){
                							// 데이터 타입은 citygml 인데 확장자는 gml 인 경우 통과
                						} else if(UploadDataType.INDOORGML == UploadDataType.findBy(dataType)
                								&& UploadDataType.GML.getValue().equalsIgnoreCase(extension)) {
                							// 데이터 타입은 indoorgml 인데 확장자는 gml 인 경우 통과
                						} else {
                							// 전부 예외
                							log.info("@@@@@@@@@@@@ datatype = {}, extension = {}", dataType, extension);
                							result.put("errorCode", "file.ext.invalid");
                							return result;
                						}
                					}
            						
            						if(UploadDataType.CITYGML == UploadDataType.findBy(dataType) && UploadDataType.INDOORGML == UploadDataType.findBy(extension)) {
            							// 전부 예외
            							log.info("@@@@@@@@@@@@ 데이터 타입이 다른 경우. datatype = {}, extension = {}", dataType, extension);
            							result.put("errorCode", "file.ext.invalid");
            							return result;
            						}
            						
            						if (UploadDataType.CITYGML.getValue().equalsIgnoreCase(dataType) && UploadDataType.GML.getValue().equalsIgnoreCase(extension)) {
                						extension = UploadDataType.CITYGML.getValue();
                					} else if (UploadDataType.INDOORGML.getValue().equalsIgnoreCase(dataType) && UploadDataType.GML.getValue().equalsIgnoreCase(extension)) {
                						extension = UploadDataType.INDOORGML.getValue();
                					}
            						// 변환 대상 파일만 이름을 변경하고 나머지 파일은 그대로 이름 유지
            						saveFileName = userId + "_" + today + "_" + System.nanoTime() + "." + extension;
                					converterTarget = true;
                					converterTargetCount++;
            					}
	        				} else {
	        					// 예외 처리
	        					log.info("@@ file.ext.invalid. extList = {}, extension = {}", uploadTypeList, fileName);
	        					result.put("errorCode", "file.ext.invalid");
	        					return result;
	        				}
            			}
            		}
            		
            		//파일 복사
            		Map<String,Object> resultMap = copyFile(zipFile.getInputStream(entry), Paths.get(directoryPath, saveFileName));
            		//
            		if(LhdtUtils.isEmpty(resultMap.get(LhdtConst.MESSAGE))) {
            			uploadDataFile.setFileType(FileType.FILE.name());
            			uploadDataFile.setFileExt(extension);
            			uploadDataFile.setFileName(fileName);
            			uploadDataFile.setFileRealName(saveFileName);
            			uploadDataFile.setFilePath(directoryPath);
            			uploadDataFile.setFileSubPath(subDirectoryPath);
            			uploadDataFile.setDepth(depth);
            			uploadDataFile.setFileSize( ""+resultMap.get("size") );
            			
            		}else {
            			uploadDataFile.setErrorMessage(""+resultMap.get(LhdtConst.MESSAGE));
            		}
            		
                }
            	
            	//
            	uploadDataFile.setConverterTarget(converterTarget);
            	uploadDataFile.setFileSize(String.valueOf(entry.getSize()));
            	log.debug("+.uploadDataFile - {}", uploadDataFile);
            	uploadDataFileList.add(uploadDataFile);
            }//entries
		} catch(RuntimeException ex) {
			log.error("{}",ex);
		} catch(IOException ex) {
			log.error("{}",ex);
		}
		
		//
		result.put("converterTargetCount", converterTargetCount);
		result.put("uploadDataFileList", uploadDataFileList);
		log.info("<< {}", result);
		return result;
	}
	
	/**
	 * @param policy
	 * @param multipartFile
	 * @return
	 */
	private static String fileValidate(Policy policy, List<String> extList, MultipartFile multipartFile) {
		
		// 2 파일 이름
		String fileName = multipartFile.getOriginalFilename();
		if(fileName == null) {
			log.info("@@ fileName is null");
			return "file.name.invalid";
		} else if(fileName.indexOf("..") >= 0 || fileName.indexOf("/") >= 0) {
			// TODO File.seperator 정규 표현식이 안 먹혀서 이렇게 처리함
			log.info("@@ fileName = {}", fileName);
			return "file.name.invalid";
		}
		
		// 3 파일 확장자
		String[] fileNameValues = fileName.split("\\.");
//		if(fileNameValues.length != 2) {
//			log.info("@@ fileNameValues.length = {}, fileName = {}", fileNameValues.length, fileName);
//			uploadLog.setError_code("fileinfo.name.invalid");
//			return uploadLog;
//		}
//		if(fileNameValues[0].indexOf(".") >= 0 || fileNameValues[0].indexOf("..") >= 0) {
//			log.info("@@ fileNameValues[0] = {}", fileNameValues[0]);
//			uploadLog.setError_code("fileinfo.name.invalid");
//			return uploadLog;
//		}
		// LowerCase로 비교
		String extension = fileNameValues[fileNameValues.length - 1];
		
		if(!extList.contains(extension.toLowerCase())) {
			log.info("@@ extList = {}, extension = {}", extList, extension);
			return "file.ext.invalid";
		}
		
		// 4 파일 사이즈
		// TODO 파일은 사이즈가 커서 제한을 해야 할지 의문?
		long fileSize = multipartFile.getSize();
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@ user upload file size = {} KB", (fileSize / 1000));
		if( fileSize > (policy.getUserUploadMaxFilesize() * 1000000l)) {
			log.info("@@ fileSize = {}, user upload max filesize = {} M", (fileSize / 1000), policy.getUserUploadMaxFilesize());
			return "file.size.invalid";
		}
		
		return null;
	}
	
	/**
	 * 업로드 데이트 수정
	 * @param request
	 * @param uploadData
	 * @param bindingResult
	 * @return
	 */
	@PostMapping(value = "/{uploadDataId:[0-9]+}")
	public Map<String, Object> update(HttpServletRequest request, @PathVariable Long uploadDataId, @Valid UploadData uploadData, BindingResult bindingResult) {
		log.info("@@ uploadData = {}", uploadData);
		Map<String, Object> result = new HashMap<>();
		String errorCode = null;
		String message = null;
		
		if(bindingResult.hasErrors()) {
			message = bindingResult.getAllErrors().get(0).getDefaultMessage();
			log.info("@@@@@ message = {}", message);
			result.put("statusCode", HttpStatus.BAD_REQUEST.value());
			result.put("errorCode", errorCode);
			result.put("message", message);
            return result;
		}
		
		if(StringUtils.isEmpty(uploadData.getDataName())) {
			errorCode = "data.name.empty";
		}
		if(StringUtils.isEmpty(uploadData.getDataGroupId())) {
			errorCode = "data.group.id.empty";
		}
		if(StringUtils.isEmpty(uploadData.getSharing())) {
			errorCode = "data.sharing.empty";
		}
		if(StringUtils.isEmpty(uploadData.getDataType())) {
			errorCode = "data.type.empty";
		}
		
		// TODO citygml, indoorgml 의 경우 위도, 경도, 높이를 포함하고 있어서 validation 체크를 하지 않음
		// 지금은 converter 가 update를 해 주지 않아서 기본 체크 함
//			if(!dataType.equals(DataType.CITYGML.getValue()) && !dataType.equals(DataType.INDOORGML.getValue())) {
		if(uploadData.getLongitude() == null) {
			errorCode = "data.longitude.empty";
		}
		if(uploadData.getLatitude() == null) {
			errorCode = "data.latitude.empty";
		}
		if(uploadData.getAltitude() == null) {
			errorCode = "data.altitude.empty";
		}
//			}
		
		if(!StringUtils.isEmpty(errorCode)) {
			log.info("@@@@@ errorCode = {}", errorCode);
			result.put("statusCode", HttpStatus.BAD_REQUEST.value());
			result.put("errorCode", errorCode);
			result.put("message", message);
            return result;
		}
	
		UserSession userSession = (UserSession)request.getSession().getAttribute(Key.USER_SESSION.name());
		
		uploadData.setLocation("POINT(" + uploadData.getLongitude() + " " + uploadData.getLatitude() + ")");
		uploadData.setUserId(userSession.getUserId());
		//uploadDataService.updateUploadData(uploadData);
		
		// 원본이 gml 파일일 경우, 데이터 타입을 citygml/indoorgml로 변경할 경우에 DB를 갱신하고 업로드 된 경로의 파일 확장자를 변경.
		// DB 갱신과 파일 확장자 변경
		uploadDataService.updateUploadDataAndFile(uploadData);
		int statusCode = HttpStatus.OK.value();
		
		result.put("statusCode", statusCode);
		result.put("errorCode", errorCode);
		result.put("message", message);
		return result;
	}
	
	/**
	 * 선택 upload-data 삭제
	 * @param request
	 * @param checkIds
	 * @param model
	 * @return
	 */
	@DeleteMapping(value = "/{uploadDataId:[0-9]+}")
	public Map<String, Object> deleteDatas(HttpServletRequest request, @PathVariable Long uploadDataId) {
		log.info("@@@@@@@ uploadDataId = {}", uploadDataId);
		Map<String, Object> result = new HashMap<>();
		String errorCode = null;
		String message = null;
		
		UserSession userSession = (UserSession)request.getSession().getAttribute(Key.USER_SESSION.name());
		UploadData uploadData = new UploadData();
		uploadData.setUserId(userSession.getUserId());
		uploadData.setUploadDataId(uploadDataId);
		
		uploadDataService.deleteUploadData(uploadData);
		int statusCode = HttpStatus.OK.value();
		
		result.put("statusCode", statusCode);
		result.put("errorCode", errorCode);
		result.put("message", message);
		return result;
	}
	
	/**
	 * validation 체크
	 * @param request
	 * @return
	 */
	private String dataValidate(MultipartHttpServletRequest request) {
		if(StringUtils.isEmpty(request.getParameter("dataName"))) {
			return "data.name.empty";
		}
		if(StringUtils.isEmpty(request.getParameter("dataGroupId"))) {
			return "data.group.id.empty";
		}
		if(StringUtils.isEmpty(request.getParameter("sharing"))) {
			return "data.sharing.empty";
		}
		
		String dataType = request.getParameter("dataType");
		if(StringUtils.isEmpty(dataType)) {
			return "data.type.empty";
		}
		
		if(	UploadDataType.CITYGML != UploadDataType.findBy(dataType)) {
			if(StringUtils.isEmpty(request.getParameter("longitude"))) {
				return "data.longitude.empty";
			}
			if(StringUtils.isEmpty(request.getParameter("latitude"))) {
				return "data.latitude.empty";
			}
			if(StringUtils.isEmpty(request.getParameter("altitude"))) {
				return "data.altitude.empty";
			}
		}
		
		Map<String, MultipartFile> fileMap = request.getFileMap();
		if(fileMap.isEmpty()) {
			return "data.file.empty";
		}
		
		return null;
	}
}
