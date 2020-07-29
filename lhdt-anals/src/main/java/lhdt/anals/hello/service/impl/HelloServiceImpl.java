/**
 * 
 */
package lhdt.anals.hello.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lhdt.anals.common.AnalsServiceImpl;
import lhdt.anals.hello.domain.Hello;
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
	public Hello findById(Long helloId) {
		// 
		return mapper.findById(helloId);
	}


	@Override
	public Hello regist(Hello vo) {
		return jpaRepo.save(vo);
	}


	@Override
	public List<Hello> findAll() {
		return mapper.findAll();
	}


	@Override
	public void updt(Hello hello) {
		jpaRepo.save(hello);
	}


	@Override
	public int findTotcnt(Hello hello) {
		return mapper.findTotcnt(hello);
	}


	@Override
	public List<Hello> findByPage(Hello hello) {
		return mapper.findByPage(hello);
	}

}
