package com.devh.example.elasticsearch8.controller;

import com.devh.example.elasticsearch8.api.response.ApiResponse;
import com.devh.example.elasticsearch8.service.ESFieldGroupByService;
import com.devh.example.elasticsearch8.vo.SampleLogSearchVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;


/**
 * <pre>
 * Description :
 *     필드 그룹바이 관련 컨트롤러
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
@RequestMapping("groupby")
@RequiredArgsConstructor
@Slf4j
public class GroupByController {
    private final ESFieldGroupByService esFieldGroupByService;

    @GetMapping("field")
    @ApiOperation(value = "sample-log 필드 그룹바", notes = "적당한 fromMillis, aggregationField을 querystring으로 전달")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "fromMillis",
                    required = true,
                    dataType = "string",
                    paramType = "query",
                    defaultValue = "1648303178000",
                    value = "시작 밀리세컨 (집계할 데이터가 너무 많으면 부하)"),
            @ApiImplicitParam(
                    name = "aggregationField",
                    required = true,
                    dataType = "string",
                    paramType = "query",
                    defaultValue = "equipName",
                    value = "집계필드"),
    })
    public ApiResponse<String> getFieldGroupBy(@ApiIgnore SampleLogSearchVO searchCommonVO) {
        try {
            log.info("[GET] groupby/field/" + searchCommonVO.getAggregationField()[0]);
            return ApiResponse.success(ApiResponse.ApiStatus.Success.OK, esFieldGroupByService.groupByField(searchCommonVO));
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.serverError(ApiResponse.ApiStatus.ServerError.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
