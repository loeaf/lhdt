package lhdt.controller.rest;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lhdt.controller.LhdtAbstractController;
import lhdt.domain.ConverterJob;
import lhdt.service.ConverterService;
import lhdt.utils.LhdtUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Data Converter
 * @author jeongdae
 *
 */
@Slf4j
@RestController
@RequestMapping("/converters")
public class ConverterRestController extends LhdtAbstractController{
	
	@Autowired
	private ConverterService converterService;
	
	/**
	 * TODO 우선은 여기서 적당히 구현해 두고... 나중에 좀 깊이 생각해 보자. converter에 어디까지 넘겨야 할지
	 * converter job insert
	 * @param model
	 * @return
	 */
	@PostMapping
	public Map<String, Object> insert(HttpServletRequest request, ConverterJob converterJob) {
		log.info("@@@ converterJob = {}", converterJob);
		
		
		//
		if(LhdtUtils.isEmpty(converterJob.getConverterCheckIds())) {
			return super.createResultMap(HttpStatus.BAD_REQUEST, "check.value.required", null);
		}

		//
		if(LhdtUtils.isEmpty(converterJob.getTitle())) {
            return super.createResultMap(HttpStatus.BAD_REQUEST, "converter.title.empty", null);
		}
		
		//
		if(LhdtUtils.isEmpty(converterJob.getUsf())) {
            return super.createResultMap(HttpStatus.BAD_REQUEST, "converter.usf.empty", null);
		}

		//
		converterJob.setUserId(super.getUserId(request));
		//
		converterService.insertConverter(converterJob);

		//
		return super.createResultMap(HttpStatus.OK, null, null);
	}
}