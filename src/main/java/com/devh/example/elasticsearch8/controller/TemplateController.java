package com.devh.example.elasticsearch8.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devh.example.elasticsearch8.api.response.ApiResponse;
import com.devh.example.elasticsearch8.service.ESTemplateService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * Description :
 *     template 관련 컨트롤러
 * ===============================================
 * Member fields :
 *     
 * ===============================================
 *
 * Author : HeonSeung Kim
 * Date   : 2022. 4. 4.
 * </pre>
 */
@RestController
@RequestMapping("template")
@RequiredArgsConstructor
@Slf4j
public class TemplateController {
	private final ESTemplateService esTemplateService;
	
	@PostMapping("update")
	@ApiOperation(value = "템플릿 재생성", notes = "템플릿 제거 후 재생성. resources에 templates.json 파일 필요")
	public ApiResponse<Boolean> postGenerateLog() {
        try {
        	log.info("[POST] template/update ...");
        	esTemplateService.updateTemplate();
            return ApiResponse.success(ApiResponse.ApiStatus.Success.OK, true);
        } catch (Exception e) {
            return ApiResponse.serverError(ApiResponse.ApiStatus.ServerError.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
