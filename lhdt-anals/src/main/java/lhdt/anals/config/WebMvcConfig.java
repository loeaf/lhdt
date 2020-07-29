/**
 * 
 */
package lhdt.anals.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lhdt.anals.interceptor.MiscInterceptor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gravity@daumsoft.com
 *
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Autowired
	private MiscInterceptor miscInterceptor;
	
	@PostConstruct
	private void init() {
		log.info("{}", this);
	}
	
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		//
		registry.addInterceptor(miscInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/", "/error", "/static/**");
	}
}
