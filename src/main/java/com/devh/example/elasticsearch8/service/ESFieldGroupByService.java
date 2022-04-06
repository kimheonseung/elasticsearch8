package com.devh.example.elasticsearch8.service;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.devh.example.elasticsearch8.component.ESClientService;
import com.devh.example.elasticsearch8.exception.ESGroupByException;
import com.devh.example.elasticsearch8.util.ExceptionUtils;
import com.devh.example.elasticsearch8.vo.SampleLogVO;
import com.devh.example.elasticsearch8.vo.SearchCommonVO;
import com.devh.example.elasticsearch8.vo.SearchResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * Description :
 *     인덱스 필드 그룹바이 서비스
 * ===============================================
 * Member fields :
 *
 * ===============================================
 *
 * Author : HeonSeung Kim
 * Date   : 2022. 4. 6.
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ESFieldGroupByService extends AbstractESSearchService<SampleLogVO> {

    private final ESClientService mESClientService;

    public List<String> groupByField(SearchCommonVO searchCommonVO) throws ESGroupByException {
        try {
            List<String> result = new ArrayList<>();
            SearchResultVO<SampleLogVO> searchResultVO = abstractSearch("sample-log", searchCommonVO, mESClientService.getElasticsearchClient(), SampleLogVO.class);
            searchResultVO.getAggregationMap().get(searchCommonVO.getAggregationField()[0])
            .forEach(aggregationVO -> {
                result.add(aggregationVO.getKey());
            });
            return result;
        } catch (Exception e) {
            throw new ESGroupByException(e.getMessage());
        }
    }

    @Override
    protected List<Query> parseQuery(SearchCommonVO searchCommonVO) {
        return new ArrayList<>();
    }

    @Override
    protected Map<String, Aggregation> parseAggregation(SearchCommonVO searchCommonVO) {
        Map<String, Aggregation> aggregationMap = new HashMap<String, Aggregation>();

        try {
            final String aggregationField = searchCommonVO.getAggregationField()[0];
            aggregationMap.put(
                    aggregationField,
                    Aggregation.of(a -> a
                            .terms(t -> t
                                    .field(aggregationField)
                                    .size(Integer.MAX_VALUE)
                            )
                    ));


        } catch (Exception e) {
            log.error(ExceptionUtils.stackTraceToString(e));
        }

        return aggregationMap;

    }

    @Override
    protected SearchResultVO<SampleLogVO> parseResponse(SearchResponse<SampleLogVO> res, SearchCommonVO searchCommonVO, long total) {
        SearchResultVO<SampleLogVO> result = new SearchResultVO<>();
        Map<String, List<SearchResultVO.AggregationVO>> aggregationMap = new HashMap<>();
        List<SearchResultVO.AggregationVO> buckets = new ArrayList<>();
        try {
            final String field = searchCommonVO.getAggregationField()[0];
            Map<String, Aggregate> aggregateMap = res.aggregations();
            final Aggregate aggregate = aggregateMap.get(field);
            aggregate.sterms().buckets().array().forEach(b -> {
                SearchResultVO.AggregationVO aggregationVO = new SearchResultVO.AggregationVO();
                aggregationVO.setKey(b.key());
                aggregationVO.setDocCount(b.docCount());
                buckets.add(aggregationVO);
            });
            aggregationMap.put(field, buckets);

            result.setAggregationMap(aggregationMap);
        } catch (Exception e) {
            log.error(ExceptionUtils.stackTraceToString(e));
        }
        System.out.println(buckets);
        return result;
    }
}
