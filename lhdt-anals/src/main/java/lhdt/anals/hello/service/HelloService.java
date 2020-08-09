/**
 * 
 */
package lhdt.anals.hello.service;

import java.util.List;

import lhdt.anals.hello.domain.SubType0;

/**
 * 안녕
 * @author gravity@daumsoft.com
 *
 */
public interface HelloService {

	/**
	 * 1건 조회
	 * @param helloId
	 * @return
	 */
	SubType0 findById(Long helloId);
	
	/**
	 * 등록
	 * @param vo
	 * @return
	 */
	SubType0 regist(SubType0 vo);

	/**
	 * 전체 목록 조회
	 * @return
	 */
	List<SubType0> findAll();

	/**
	 * @param hello
	 */
	void updt(SubType0 hello);

	/**
	 * 전체 검색 건수
	 * @param hello
	 * @return
	 */
	int findTotcnt(SubType0 hello);
	
	/**
	 * 페이징
	 * @param hello
	 * @return
	 */
	List<SubType0> findByPage(SubType0 hello);
}
