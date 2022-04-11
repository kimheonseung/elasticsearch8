package com.devh.example.elasticsearch8.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devh.example.elasticsearch8.api.response.ApiResponse;
import com.devh.example.elasticsearch8.service.ESSampleLogAggregationService;
import com.devh.example.elasticsearch8.vo.SampleLogSearchVO;
import com.devh.example.elasticsearch8.vo.SampleLogVO;
import com.devh.example.elasticsearch8.vo.SearchResultVO;
import com.devh.example.elasticsearch8.vo.SearchResultVO.AggregationVO;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <pre>
 * Description :
 *     sample-log 집계 관련 컨트롤러
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
@RequestMapping("aggregation")
@RequiredArgsConstructor
@Slf4j
public class AggregationController {
	private final ESSampleLogAggregationService esSampleLogAggregationService;
	
	@GetMapping("sample-log")
	@ApiOperation(value = "sample-log 집계", notes = "적당한 fromMillis, aggregation=true, aggregationType, aggregationField, aggregationTopN을 querystring으로 전달")
	@ApiImplicitParams({
		@ApiImplicitParam(
				name = "fromMillis",
				required = true,
				dataType = "string",
				paramType = "query",
				defaultValue = "1648303178000",
				value = "시작 밀리세컨 (집계할 데이터가 너무 많으면 부하)"),
		@ApiImplicitParam(
				name = "aggregation", 
				required = true,
				dataType = "string",
				paramType = "query",
				defaultValue = "true",
				value = "집계여부 (기본 false)"),
		@ApiImplicitParam(
				name = "aggregationType", 
				required = true,
				dataType = "string",
				paramType = "query",
				defaultValue = "term,term,time",
				value = "집계타입"),
		@ApiImplicitParam(
				name = "aggregationField", 
				required = true,
				dataType = "string",
				paramType = "query",
				defaultValue = "equipName,logName,timeMillis",
				value = "집계필드"),
		@ApiImplicitParam(
				name = "aggregationTopN", 
				required = true,
				dataType = "string",
				paramType = "query",
				defaultValue = "5,10,10",
				value = "집계 탑 갯수"),
	})
	public ApiResponse<Map<String, List<AggregationVO>>> getSampleLog(@ApiIgnore SampleLogSearchVO sampleLogSearchVO) {
		try {
        	log.info("[GET] aggregation/sample-log ... " + sampleLogSearchVO);
        	SearchResultVO<SampleLogVO> result = esSampleLogAggregationService.aggregate(sampleLogSearchVO);
            return ApiResponse.success(ApiResponse.ApiStatus.Success.OK, result.getAggregationMap());
        } catch (Exception e) {
            return ApiResponse.serverError(ApiResponse.ApiStatus.ServerError.INTERNAL_SERVER_ERROR, e.getMessage());
        }
	}

}
