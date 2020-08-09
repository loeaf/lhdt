package lhdt.utils;

import java.io.File;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class FilePathTest {

	@Test
	void test() {
//		File file = Paths.get("/f4d/test", "aa").toFile();
//		System.out.println(file.getPath());
		
		String dataGroupPath = "basic/";
		
		String[] directors = dataGroupPath.split("/");
		String fullName = "D:\\data\\mago3d\\f4d\\";
		
		boolean result = true;
		for(String directoryName : directors) {
			fullName = fullName + directoryName + File.separator;
			File directory = new File(fullName);
			log.info("----------- fullName = {}", fullName);
		}
	}

}
