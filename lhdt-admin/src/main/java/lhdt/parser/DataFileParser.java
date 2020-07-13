package lhdt.parser;

import java.util.Map;

import lhdt.domain.DataFileInfo;

/**
 * @author Cheon JeongDae
 *
 */
public interface DataFileParser {
	
	/**
	 * @param dataGroupId
	 * @param fileInfo
	 * @return
	 */
	Map<String, Object> parse(Integer dataGroupId, DataFileInfo fileInfo);
}
