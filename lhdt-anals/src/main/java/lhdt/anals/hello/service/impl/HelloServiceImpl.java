/**
 * 
 */
package lhdt.anals.hello.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lhdt.anals.common.AnalsServiceImpl;
import lhdt.anals.hello.domain.SubType0;
import lhdt.anals.hello.persistence.HelloMapper;
import lhdt.anals.hello.persistence.HelloRepository;
import lhdt.anals.hello.service.HelloService;

/**
 * 안녕 service impl
 * @author gravity@daumsoft.com
 *
 */
@Service("helloService")
public class HelloServiceImpl extends AnalsServiceImpl implements HelloService {


	/**
	 * jpa
	 */
	@Autowired
	private HelloRepository jpaRepo;
	
	/**
	 * mybatis
	 */
	@Autowired
	private HelloMapper mapper;
	
	
	@Override
	public SubType0 findById(Long helloId) {
		// 
		return mapper.findById(helloId);
	}


	@Override
	public SubType0 regist(SubType0 vo) {
		return jpaRepo.save(vo);
	}


	@Override
	public List<SubType0> findAll() {
		return mapper.findAll();
	}


	@Override
	public void updt(SubType0 hello) {
		jpaRepo.save(hello);
	}


	@Override
	public int findTotcnt(SubType0 hello) {
		return mapper.findTotcnt(hello);
	}


	@Override
	public List<SubType0> findByPage(SubType0 hello) {
		return mapper.findByPage(hello);
	}

}
