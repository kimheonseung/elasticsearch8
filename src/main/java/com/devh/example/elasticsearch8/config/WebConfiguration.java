package com.devh.example.elasticsearch8.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	/**
	 * <pre>
	 * Description :
	 *     API 서버로서 자원을 공유하도록 설정
	 * ===============================================
	 * Parameters :
	 *     
	 * Returns :
	 *     
	 * Throws :
	 *     
	 * ===============================================
	 *
	 * Author : HeonSeung Kim
	 * Date   : 2022. 4. 6.
	 * </pre>
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
        	.addMapping("/**")
        	.allowedOrigins("http://localhost:3000")
        	.allowedMethods("GET", "POST");
	}
	
}
