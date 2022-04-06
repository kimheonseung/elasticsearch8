package com.devh.example.elasticsearch8.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.devh.example.elasticsearch8.api.vo.PagingVO;
import com.devh.example.elasticsearch8.component.ESClientService;
import com.devh.example.elasticsearch8.exception.ESSearchException;
import com.devh.example.elasticsearch8.util.ExceptionUtils;
import com.devh.example.elasticsearch8.vo.SampleLogSearchVO;
import com.devh.example.elasticsearch8.vo.SampleLogVO;
import com.devh.example.elasticsearch8.vo.SearchCommonVO;
import com.devh.example.elasticsearch8.vo.SearchResultVO;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * Description :
 *     sample-log 인덱스 관련 검색 서비스
 * ===============================================
 * Member fields :
 *     
 * ===============================================
 *
 * Author : HeonSeung Kim
 * Date   : 2022. 4. 3.
 * </pre>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ESSampleLogSearchService extends AbstractESSearchService<SampleLogVO> {
	private final ESClientService mESClientService;
	
	/**
	 * <pre>
	 * Description :
	 *     요청 변수에 따라 검색 수행
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
	public SearchResultVO<SampleLogVO> search(SearchCommonVO searchCommonVO) throws ESSearchException {
		try {
			return abstractSearch("sample-log", searchCommonVO, mESClientService.getElasticsearchClient(), SampleLogVO.class);
		} catch (ESSearchException e) {
			throw new ESSearchException(e.getMessage());
		}
	}
	
	@Override
	protected List<Query> parseQuery(SearchCommonVO searchCommonVO) {
		
		List<Query> queryList = new ArrayList<Query>();
		
		try {
			SampleLogSearchVO sampleLogSearchVO = (SampleLogSearchVO) searchCommonVO;
			
			final String[] ips = sampleLogSearchVO.getIp();
			final String[] ipOperators = sampleLogSearchVO.getIpOperator();
			if(ips.length > 0) {
				
				queryList.add(Query.of(q -> q
						.bool(b -> {
							for(int i = 0 ; i < ips.length ; ++i) {
								final String value = ips[i];
								final String operator = ipOperators[i];
								switch(operator) {
								case "eq":
									b.should(s -> s
											.term(TermQuery.of(t -> t
													.field(SampleLogVO.IP)
													.value(value)
													))
											);
									break;
								default:
									break;
								}
							}
							return b;
							})
						)
				);
				
			}
			
			
			final String[] equipNames = sampleLogSearchVO.getEquipName();
			final String[] equipNameOperators = sampleLogSearchVO.getEquipNameOperator();
			if(equipNames.length > 0) {
				queryList.add(Query.of(q -> q
						.bool(b -> {
							for(int i = 0 ; i < equipNames.length ; ++i) {
								final String value = equipNames[i];
								final String operator = equipNameOperators[i];
								switch(operator) {
								case "eq":
									b.should(s -> s
											.term(TermQuery.of(t -> t
													.field(SampleLogVO.EQUIP_NAME)
													.value(value)
													))
											);
									break;
								default:
									break;
								}
							}
							return b;
							})
						)
				);
			}
			
			final String[] logPaths = sampleLogSearchVO.getLogPath();
			final String[] logPathOperators = sampleLogSearchVO.getLogPathOperator();
			if(logPaths.length > 0) {
				queryList.add(Query.of(q -> q
						.bool(b -> {
							for(int i = 0 ; i < logPaths.length ; ++i) {
								final String value = logPaths[i];
								final String operator = logPathOperators[i];
								switch(operator) {
								case "eq":
									b.should(s -> s
											.term(TermQuery.of(t -> t
													.field(SampleLogVO.LOG_PATH)
													.value(value)
													))
											);
									break;
								default:
									break;
								}
							}
							return b;
							})
						)
				);
			}
			final String[] logNames = sampleLogSearchVO.getLogName();
			final String[] logNameOperators = sampleLogSearchVO.getLogNameOperator();
			if(logNames.length > 0) {
				queryList.add(Query.of(q -> q
						.bool(b -> {
							for(int i = 0 ; i < logNames.length ; ++i) {
								final String value = logNames[i];
								final String operator = logNameOperators[i];
								switch(operator) {
								case "eq":
									b.should(s -> s
											.term(TermQuery.of(t -> t
													.field(SampleLogVO.LOG_NAME)
													.value(value)
													))
											);
									break;
								default:
									break;
								}
							}
							return b;
							})
						)
				);
			}
			
			final String[] logs = sampleLogSearchVO.getLog();
			final String[] logOperators = sampleLogSearchVO.getLogOperator();
			if(logs.length > 0) {
				queryList.add(Query.of(q -> q
						.bool(b -> {
							for(int i = 0 ; i < logs.length ; ++i) {
								final String value = logs[i];
								final String operator = logOperators[i];
								switch(operator) {
								case "eq":
									b.should(s -> s
											.term(TermQuery.of(t -> t
													.field(SampleLogVO.LOG)
													.value(value)
													))
											);
									break;
								default:
									break;
								}
							}
							return b;
							})
						)
				);
			}
		} catch (Exception e) {
			log.error(ExceptionUtils.stackTraceToString(e));
		}
		
		return queryList;
	}

	@Override
	protected Map<String, Aggregation> parseAggregation(SearchCommonVO searchCommonVO) {
		return new HashMap<String, Aggregation>();
	}

	@Override
	protected SearchResultVO<SampleLogVO> parseResponse(SearchResponse<SampleLogVO> res, SearchCommonVO searchCommonVO, long total) {
		List<SampleLogVO> list = new ArrayList<SampleLogVO>();
		res.hits().hits().forEach(h -> {
			list.add(h.source());
		});
		
		SearchResultVO<SampleLogVO> searchResultVO = new SearchResultVO<SampleLogVO>();
		searchResultVO.setList(list);
		searchResultVO.setTotal(total);
		searchResultVO.setPaging(PagingVO.build(searchCommonVO.getPage(), searchCommonVO.getPageSize(), searchCommonVO.getRows(), total));
		return searchResultVO;
	}
}
