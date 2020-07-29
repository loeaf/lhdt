/**
 * 
 */
package lhdt.anals.hello.persistence;

import java.util.List;

import lhdt.anals.config.AnalsConnMapper;
import lhdt.anals.hello.domain.Hello;

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
	Hello findById(Long helloId);

	/**
	 * 전체 목록
	 * @return
	 */
	List<Hello> findAll();
	
	/**
	 * 전체 건수
	 * @param hello
	 * @return
	 */
	Integer findTotcnt(Hello hello);
	
	
	/**
	 * 페이징
	 * @param hello
	 * @return
	 */
	List<Hello> findByPage(Hello hello);
}
