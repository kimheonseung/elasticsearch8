package com.devh.example.elasticsearch8.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devh.example.elasticsearch8.api.response.ApiResponse;
import com.devh.example.elasticsearch8.service.ESSampleLogSearchService;
import com.devh.example.elasticsearch8.vo.SampleLogSearchVO;
import com.devh.example.elasticsearch8.vo.SampleLogVO;
import com.devh.example.elasticsearch8.vo.SearchResultVO;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <pre>
 * Description :
 *     sample-log 감섹 관련 컨트롤러
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
@RequestMapping("search")
@RequiredArgsConstructor
@Slf4j
public class SearchController {
	private final ESSampleLogSearchService esSampleLogSearchService;
	
	@GetMapping("sample-log")
	@ApiOperation(value = "샘플로그 검색", notes = "검색 변수 querystring으로 변환 필요")
	@ApiImplicitParams({
		@ApiImplicitParam(
				name = "fromMillis", 
				required = true,
				dataType = "string",
				paramType = "query",
				defaultValue = "1648303178000",
				value = "시작 밀리세컨"),
		@ApiImplicitParam(
				name = "ip", 
				dataType = "string",
				paramType = "query",
				defaultValue = "192.168.100.1,192.168.100.2",
				value = "ip 검색대상 (서로 OR 연산)"),
		@ApiImplicitParam(
				name = "ipOperator", 
				dataType = "string",
				paramType = "query",
				defaultValue = "eq,eq",
				value = "ip 검색대상의 연산자"),
		@ApiImplicitParam(
				name = "logPath", 
				dataType = "string",
				paramType = "query",
				value = "logPath 검색대상 (서로 OR 연산)"),
		@ApiImplicitParam(
				name = "logPathOperator", 
				dataType = "string",
				paramType = "query",
				value = "logPath 검색대상의 연산자"),
		@ApiImplicitParam(
				name = "logName", 
				dataType = "string",
				paramType = "query",
				value = "logName 검색대상 (서로 OR 연산)"),
		@ApiImplicitParam(
				name = "logNameOperator", 
				dataType = "string",
				paramType = "query",
				value = "logName 검색대상의 연산자"),
		@ApiImplicitParam(
				name = "log", 
				dataType = "string",
				paramType = "query",
				value = "log 검색대상 (서로 OR 연산)"),
		@ApiImplicitParam(
				name = "logOperator", 
				dataType = "string",
				paramType = "query",
				value = "log 검색대상의 연산자"),
		@ApiImplicitParam(
				name = "page", 
				dataType = "string",
				paramType = "query",
				defaultValue = "2",
				value = "검색할 페이지 (기본 1)"),
		@ApiImplicitParam(
				name = "pageSize", 
				dataType = "string",
				paramType = "query",
				defaultValue = "5",
				value = "보여질 페이지의 갯수 (기본 10)"),
		@ApiImplicitParam(
				name = "rows", 
				dataType = "string",
				paramType = "query",
				defaultValue = "20",
				value = "페이지 검색결과 데이터 갯수 (기본 10)"),
		@ApiImplicitParam(
				name = "sortIndex", 
				dataType = "string",
				paramType = "query",
				value = "결과 정렬 기준 (기본 timeMillis)"),
		@ApiImplicitParam(
				name = "sortOrder", 
				dataType = "string",
				paramType = "query",
				value = "결과 정렬 순서 (기본 Desc)"),
		@ApiImplicitParam(
				name = "toMillis", 
				dataType = "string",
				paramType = "query",
				value = "끝 밀리세컨 (기본 현재시간)"),
	})
	public ApiResponse<SampleLogVO> getSampleLog(@ApiIgnore SampleLogSearchVO sampleLogSearchVO) {
		try {
        	log.info("[GET] search/sample-log ... " + sampleLogSearchVO);
        	SearchResultVO<SampleLogVO> searchResultVO = esSampleLogSearchService.search(sampleLogSearchVO);
            return ApiResponse.success(ApiResponse.ApiStatus.Success.OK, searchResultVO.getList(), searchResultVO.getPaging());
        } catch (Exception e) {
            return ApiResponse.serverError(ApiResponse.ApiStatus.ServerError.INTERNAL_SERVER_ERROR, e.getMessage());
        }
	}

}
