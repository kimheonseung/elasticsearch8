package com.devh.example.elasticsearch8.service;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import co.elastic.clients.elasticsearch._types.aggregations.DateHistogramAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.devh.example.elasticsearch8.component.ESClientService;
import com.devh.example.elasticsearch8.exception.ESSearchException;
import com.devh.example.elasticsearch8.vo.SampleLogVO;
import com.devh.example.elasticsearch8.vo.SearchCommonVO;
import com.devh.example.elasticsearch8.vo.SearchResultVO;
import com.devh.example.elasticsearch8.vo.SearchResultVO.AggregationVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <pre>
 * Description :
 *     sample-log 인덱스 관련 집계 서비스
 * ===============================================
 * Member fields :
 *     
 * ===============================================
 *
 * Author : HeonSeung Kim
 * Date   : 2022. 4. 3.
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class ESSampleLogAggregationService extends AbstractESSearchService<SampleLogVO> {
	private final ESClientService mESClientService;
	
	/**
	 * <pre>
	 * Description :
	 *     요청변수에 따라 집계 수행
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
	 * Date   : 2022. 4. 4.
	 * </pre>
	 */
	public SearchResultVO<SampleLogVO> aggregate(SearchCommonVO searchCommonVO) throws ESSearchException {
		try {
			return abstractSearch("sample-log", searchCommonVO, mESClientService.getElasticsearchClient(), SampleLogVO.class);
		} catch (ESSearchException e) {
			throw new ESSearchException(e.getMessage());
		}
	}

	@Override
	protected List<Query> parseQuery(SearchCommonVO searchCommonVO) {
		return new ArrayList<Query>();
	}
	
	@Override
	protected Map<String, Aggregation> parseAggregation(SearchCommonVO searchCommonVO) {
		
		Map<String, Aggregation> aggregationMap = new HashMap<String, Aggregation>();
		
		if(searchCommonVO.isAggregation()) {
			final String[] aggregationTypes = searchCommonVO.getAggregationType();
			final String[] aggregationFields = searchCommonVO.getAggregationField();
			final int[] aggregationTopNs = searchCommonVO.getAggregationTopN();
			
			for(int i = 0 ; i < aggregationTypes.length ; ++i) {
				final String aggregationType = aggregationTypes[i];
				final String aggregationField = aggregationFields[i];
				final int aggregationTopN = aggregationTopNs[i];
				
				switch (aggregationType) {
				case "term":
					aggregationMap.put(
							String.format("%s_%s_%d", aggregationField, aggregationType, aggregationTopN),
							Aggregation.of(a -> a
									.terms(t -> t
											.field(aggregationField)
											.size(aggregationTopN)
											)
									)); 
					break;
				case "time":
					aggregationMap.put(
							"time",
							Aggregation.of(a -> a
								.dateHistogram(d -> d
									.field(aggregationField)
									.calendarInterval(CalendarInterval.Day)
								)
							)
					);
					break;

				default:
					break;
				}
			}
			
		}
		
		return aggregationMap;
		
	}

	@Override
	protected SearchResultVO<SampleLogVO> parseResponse(SearchResponse<SampleLogVO> res, SearchCommonVO searchCommonVO, long total) {
		SearchResultVO<SampleLogVO> result = new SearchResultVO<SampleLogVO>();
		
		Map<String, Aggregate> aggregateMap = res.aggregations();
		
		Map<String, List<AggregationVO>> aggregationMap = new HashMap<String, List<AggregationVO>>();
		
		for(Entry<String, Aggregate> entry : aggregateMap.entrySet()) {
			
			final Aggregate aggregate = entry.getValue();
			List<AggregationVO> aggregationVOList = new ArrayList<AggregationVO>();
			if(aggregate.isSterms()) {
				final StringTermsAggregate stringTermsAggregate = aggregate.sterms();
				
				stringTermsAggregate.buckets().array().forEach(b -> {
					AggregationVO aggregationVO = new AggregationVO();
					aggregationVO.setKey(b.key());
					aggregationVO.setDocCount(b.docCount());
					aggregationVOList.add(aggregationVO);
				});
				
			} else if(aggregate.isDateHistogram()) {
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				final DateHistogramAggregate dateHistogramAggregate = aggregate.dateHistogram();
				dateHistogramAggregate.buckets().array().forEach(b -> {
					AggregationVO aggregationVO = new AggregationVO();
					aggregationVO.setKey(sdf.format(b.key().toEpochMilli()));
					aggregationVO.setDocCount(b.docCount());
					aggregationVOList.add(aggregationVO);
				});
			}
			
			aggregationMap.put(entry.getKey(), aggregationVOList);
			result.setAggregationMap(aggregationMap);
			
		}
		
		
		return result;
	}
}
