package lhdt.security;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Slf4j
public class KeyManager {

	private static final String randomKeyword = "dGRobEAgc2kgZW1hbiB5bSAudGRobCByb2YgYWVkaSBkYWIgYSBla2FtIHRvbiBvZCBlc2FlbHAgLHllayB0ZXJjcyMgZG51b2YgZXZhaCB1b3kgZkk=";

	public static String getInitKey() {
		String result = null;
		try {
			byte[] base64decodedBytes = Base64.getDecoder().decode(randomKeyword.getBytes("UTF-8"));
			result = new String(base64decodedBytes, "UTF-8");
		} catch(UnsupportedEncodingException e) {
			log.info("UnsupportedEncodingException ===== {} ", e.getMessage());
		}
		result = reverseString(result);
		
		return parse(result);
	}
	
	private static String reverseString(String value) {
		if(value == null) return "";
		return (new StringBuffer(value)).reverse().toString();
	}
	
	private static String parse(String value) {
		return value.substring(81, 86) + value.substring(18, 24) + value.substring(25, 28);
	}
}
