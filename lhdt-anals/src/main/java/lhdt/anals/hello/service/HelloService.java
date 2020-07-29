/**
 * 
 */
package lhdt.anals.hello.service;

import java.util.List;

import lhdt.anals.common.AnalsService;
import lhdt.anals.hello.domain.Hello;

/**
 * 안녕
 * @author gravity@daumsoft.com
 *
 */
public interface HelloService extends AnalsService {

	/**
	 * 1건 조회
	 * @param helloId
	 * @return
	 */
	Hello findById(Long helloId);
	
	/**
	 * 등록
	 * @param vo
	 * @return
	 */
	Hello regist(Hello vo);

	/**
	 * 전체 목록 조회
	 * @return
	 */
	List<Hello> findAll();

	/**
	 * @param hello
	 */
	void updt(Hello hello);

	/**
	 * 전체 검색 건수
	 * @param hello
	 * @return
	 */
	int findTotcnt(Hello hello);
	
	/**
	 * 페이징
	 * @param hello
	 * @return
	 */
	List<Hello> findByPage(Hello hello);
}
