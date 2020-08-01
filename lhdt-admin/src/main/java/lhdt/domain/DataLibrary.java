package lhdt.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Data library 정보
 * @author Cheon JeongDae
 *
 */
@ToString(callSuper = true)
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataLibrary extends Search implements Serializable {
	
	public static final String F4D_PREFIX = "F4D_";
	private static final long serialVersionUID = 5291627645684846695L;

	/******** 화면 오류 표시용 ********/

	// 사용자명
	private String userId;
	private String userName;
	
	/****** validator ********/
	private MethodType methodType;

	// data library 고유번호
	private Long dataLibraryId;
	// data libray Group 고유번호
	private Integer dataLibraryGroupId;
	// data library key
	private String dataLibraryKey;
	// data library name
	private String dataLibraryName;
	// 서비스 타입(정적, primitive)
	private String serviceType;
	// 순서
	private Integer viewOrder;
	// 사용 유무
	private Boolean available;

	// 설명
	private String description;
	
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime viewUpdateDate;
	
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime viewInsertDate;
	
	public LocalDateTime getViewUpdateDate() {
		return this.updateDate;
	}
	public LocalDateTime getViewInsertDate() {
		return this.insertDate;
	}
	
	// 수정일 
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateDate;
	// 등록일
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime insertDate;
}
