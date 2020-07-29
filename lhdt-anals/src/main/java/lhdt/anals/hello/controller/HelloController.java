/**
 * 
 */
package lhdt.anals.hello.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lhdt.anals.common.AnalsConst;
import lhdt.anals.common.AnalsController;
import lhdt.anals.common.AnalsUtils;
import lhdt.anals.hello.domain.Hello;
import lhdt.anals.hello.service.HelloService;

/**
 * sample controller
 * @author gravity@daumsoft.com
 *
 */
@RestController
@RequestMapping("/hello/")
public class HelloController extends AnalsController {
	@Autowired
	private HelloService service;

	@GetMapping()
	public ResponseEntity<Map<String,Object>> helloWorld(){
		
		//
		Map<String,Object> map = new HashMap<>();
		
		//
		Long helloId = AnalsUtils.getUniqueLong();
		
		//
		Hello h1 = Hello.builder()
				.helloId(helloId)
				.helloName("hello world")
				.cn(new Date().toString())
				.build();
		
		
		//등록 by jpa
		Hello saveResult = service.regist(h1);
		map.put("saveResult", saveResult);
		
		//수정 by jpa
		service.updt(h1);
		
		//조회 by mybatis
		Hello data = service.findById(helloId);
		map.put(AnalsConst.DATA, data);
		
		//전체목록 by mybatis
		List<Hello> datas = service.findAll();
		map.put(AnalsConst.DATAS, datas);
		
		//검색,페이징 조건
		Hello searchHello = new Hello();
		searchHello.setSearchHelloName("world");
		searchHello.setOffset(0); //0부터 시작
		searchHello.setPageSize(10);
		
		//검색 건수
		int totcnt = service.findTotcnt(searchHello);
		map.put(AnalsConst.TOTCNT, totcnt);
		
		//페이징
		List<Hello> pagingDatas = service.findByPage(searchHello);
		map.put("pagingDatas", pagingDatas);
		
		//
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
}
