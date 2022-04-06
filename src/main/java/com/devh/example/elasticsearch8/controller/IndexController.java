package com.devh.example.elasticsearch8.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devh.example.elasticsearch8.api.response.ApiResponse;
import com.devh.example.elasticsearch8.service.ESIndexService;
import com.devh.example.elasticsearch8.vo.IndexVO;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * Description :
 *     index 관련 컨트롤러
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
@RequestMapping("index")
@RequiredArgsConstructor
@Slf4j
public class IndexController {
	private final ESIndexService esIndexService;
	
	@PostMapping("create/sample")
	@ApiOperation(value = "샘플로그로 인덱스 생성", notes = "testlog.json 파일 필요")
	public ApiResponse<Boolean> postGenerateLog() {
        try {
        	log.info("[POST] index/create/sample ...");
        	esIndexService.startESIndexConsumerThread();
			esIndexService.loadSampleLog();
			esIndexService.loadSampleEvent();
            return ApiResponse.success(ApiResponse.ApiStatus.Success.OK, true);
        } catch (Exception e) {
            return ApiResponse.serverError(ApiResponse.ApiStatus.ServerError.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
	
	@PostMapping("delete/sample")
	@ApiOperation(value = "샘플로그로 인덱스 삭제", notes = "sample로 시작하는 인덱스 모두 제거")
	public ApiResponse<Boolean> postDeleteLog() {
		try {
			log.info("[POST] index/delete/sample ...");
			return ApiResponse.success(ApiResponse.ApiStatus.Success.OK, esIndexService.deleteSample());
		} catch (Exception e) {
			return ApiResponse.serverError(ApiResponse.ApiStatus.ServerError.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	@GetMapping("list")
	@ApiOperation(value = "인덱스 상태 및 목록 조회", notes = "_cat/indices")
	public ApiResponse<IndexVO> getIndexList() {
		try {
        	log.info("[GET] index/list ...");
            return ApiResponse.success(ApiResponse.ApiStatus.Success.OK, esIndexService.getIndices());
        } catch (Exception e) {
            return ApiResponse.serverError(ApiResponse.ApiStatus.ServerError.INTERNAL_SERVER_ERROR, e.getMessage());
        }
	}
	
}
