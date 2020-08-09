/**
 * 
 */
package lhdt.anals.hello.persistence;

import java.util.List;

import lhdt.anals.config.AnalsConnMapper;
import lhdt.anals.hello.domain.SubType0;

/**
 * 데이터 조회전용 mybatis mapper
 * @author gravity@daumsoft.com
 *
 */
@AnalsConnMapper
public interface HelloMapper {

	/**
	 * 1개 조회
	 * @param helloId
	 * @return
	 */
	SubType0 findById(Long helloId);

	/**
	 * 전체 목록
	 * @return
	 */
	List<SubType0> findAll();
	
	/**
	 * 전체 건수
	 * @param hello
	 * @return
	 */
	Integer findTotcnt(SubType0 hello);
	
	
	/**
	 * 페이징
	 * @param hello
	 * @return
	 */
	List<SubType0> findByPage(SubType0 hello);
}
