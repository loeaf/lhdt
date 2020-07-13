package lhdt.parser;

import java.util.Map;

import lhdt.domain.DataAttributeFileInfo;

/**
 * @author Cheon JeongDae
 *
 */
public interface DataAttributeFileParser {
	
	/**
	 * parse
	 * @param dataId
	 * @param fileInfo
	 * @return
	 */
	Map<String, Object> parse(Long dataId, DataAttributeFileInfo dataAttributeFileInfo);
}
