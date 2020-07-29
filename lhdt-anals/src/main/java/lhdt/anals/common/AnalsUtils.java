/**
 * 
 */
package lhdt.anals.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import dev.hyunlab.core.util.PpUtil;

/**
 * 이것저것 유틸리티
 * @author gravity@daumsoft.com
 *
 */
public class AnalsUtils extends PpUtil{
	
	/**
	 * unique한  long값 생성 & 리턴
	 * @return
	 */
	public static Long getUniqueLong() {
		return (new Date()).getTime();
	}
	
	/**
	 * result map 생성
	 * @return
	 */
	public static Map<String,Object> createResultMap(){
		return createResultMap(HttpStatus.OK, null, null);
	}
	
	/**
	 * result map 생성
	 * @param statusCode
	 * @param errorCode
	 * @param message
	 * @return
	 */
	public static Map<String,Object> createResultMap(HttpStatus statusCode, String errorCode, String message){
		Map<String,Object> map = new HashMap<>();
		
		//
		map.put(AnalsConst.STATUS_CODE, statusCode);
		map.put(AnalsConst.ERROR_CODE, errorCode);
		map.put(AnalsConst.MESSAGE, message);
		
		//
		return map;
	}
}
