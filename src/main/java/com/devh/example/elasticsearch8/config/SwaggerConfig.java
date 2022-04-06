package com.devh.example.elasticsearch8.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <pre>
 * Description :
 *     swagger 설정 클래스
 * ===============================
 * Memberfields :
 *     
 * ===============================
 * 
 * Author : HeonSeung Kim
 * Date   : 2022. 4. 5.
 * </pre>
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	private static final String API_NAME = "Elasticsearch8";
	private static final String API_VERSION = "v1";
	private static final String API_DESCRIPTION = "ES8 Java API 샘플코드";
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(new ApiInfoBuilder()
						.title(API_NAME)
						.version(API_VERSION)
						.description(API_DESCRIPTION)
						.build())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.devh.example.elasticsearch8.controller"))
				.paths(PathSelectors.any())
				.build();
	}
}
