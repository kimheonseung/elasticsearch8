package com.devh.example.elasticsearch8.service;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import co.elastic.clients.elasticsearch.core.search.HighlightField;
import com.devh.example.elasticsearch8.exception.ESSearchException;
import com.devh.example.elasticsearch8.util.ExceptionUtils;
import com.devh.example.elasticsearch8.vo.SearchCommonVO;
import com.devh.example.elasticsearch8.vo.SearchResultVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import jakarta.json.stream.JsonGenerator;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * Description :
 *     Elasticsearch 검색공통 추상화 클래스
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
public abstract class AbstractESSearchService<T> {

    /**
     * <pre>
     * Description :
     *     검색변수를 파싱해 쿼리 리스트 생성
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
     * Date   : 2022. 4. 3.
     * </pre>
     */
    protected abstract List<Query> parseQuery(SearchCommonVO searchCommonVO);

    /**
     * <pre>
     * Description :
     *     검색변수를 파싱해 집계 쿼리 생성
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
     * Date   : 2022. 4. 3.
     * </pre>
     */
    protected abstract Map<String, Aggregation> parseAggregation(SearchCommonVO searchCommonVO);

    /**
     * <pre>
     * Description :
     *     검색결과를 가공
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
    protected abstract SearchResultVO<T> parseResponse(SearchResponse<T> res, SearchCommonVO searchCommonVO, long total);

    /**
     * <pre>
     * Description :
     *     상속객체에서 구현한 추상메소드를 통해 검색로직 수행
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
     * Date   : 2022. 4. 3.
     * </pre>
     */
    protected SearchResultVO<T> abstractSearch(String indexName, SearchCommonVO searchCommonVO, ElasticsearchClient client, Class<T> cls) throws ESSearchException {
        StringBuffer sbLog = new StringBuffer();
        try {

            List<Query> queryList = parseQuery(searchCommonVO);
            List<String> indices = getIndices(indexName, searchCommonVO.getFromMillis(), searchCommonVO.getToMillis());

            SearchRequest req = SearchRequest.of(sr -> sr
                    .trackTotalHits(h -> h
                            .enabled(Boolean.TRUE)
                    )
                    .size(searchCommonVO.getRows())
                    .from(searchCommonVO.getRows() * (searchCommonVO.getPage() - 1))
                    .index(indices)
                    .sort(st -> st
                            .field(
                                    FieldSort.of(f -> f
                                            .field(searchCommonVO.getSortIndex())
                                            .order(SortOrder.valueOf(searchCommonVO.getSortOrder()))
                                    )
                            )
                    )
                    .query(q -> q
                            .bool(b -> b
                                    .must(m -> m
                                            .range(r -> r
                                                    .field(searchCommonVO.getSortIndex())
                                                    .gte(JsonData.of(searchCommonVO.getFromMillis()))
                                                    .lte(JsonData.of(searchCommonVO.getToMillis()))
                                            )
                                    )
                                    .must(queryList)
                            )
                    )
                    .highlight(h -> {
                        if(searchCommonVO.isHighlight()) {
                            for(int idx = 0 ; idx < searchCommonVO.getHighlightField().length ; ++idx) {
                                final String field = searchCommonVO.getHighlightField()[idx];
                                final String keyword = searchCommonVO.getHighlightKeyword()[idx];
                                h
                                .fields(field, HighlightField.of(hf -> hf
                                        .preTags("@")
                                        .postTags("@")
                                        .highlightQuery(hq -> hq
                                                .term(tq -> tq
                                                        .field(field)
                                                        .value(keyword)
                                                )
                                        )
                                ))
                                .fragmentSize(1024);
                            }
                        }
                        return h;
                    })
                    .aggregations(parseAggregation(searchCommonVO))
            );

            appendRequestLog(sbLog, req, client);
            SearchResponse<T> res = client.search(req, cls);

            final long total = res.hits().total().value();
            final long took = res.took();

            appendResponseLog(sbLog, total, took);

            SearchResultVO<T> searchResult = parseResponse(res, searchCommonVO, total);

            sbLog.insert(0, "\n======= Elasticsearch Request & Response =======\n");

            log.info(sbLog.toString());

            return searchResult;
        } catch (Exception e) {
            sbLog.insert(0, "\n======= Elasticsearch Request & Response =======\n");
            log.warn(sbLog.toString());
            log.error(ExceptionUtils.stackTraceToString(e));
            throw new ESSearchException(e.getMessage());
        }
    }


    /**
     * <pre>
     * Description :
     *     검색할 인덱스 리스트 생성
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
     * Date   : 2022. 4. 3.
     * </pre>
     */
    protected List<String> getIndices(String indexName, long fromMillis, long toMillis) {
        List<String> indices = new ArrayList<String>();

        Calendar fromCal = Calendar.getInstance();
        Calendar toCal = Calendar.getInstance();
        fromCal.setTimeInMillis(fromMillis);

        toCal.setTimeInMillis(toMillis);

        if (fromCal.get(Calendar.YEAR) == toCal.get(Calendar.YEAR) &&
                fromCal.get(Calendar.MONTH) == toCal.get(Calendar.MONTH)) {
            indices.add(indexName + "*" + String.format("%4d-%02d", fromCal.get(Calendar.YEAR), fromCal.get(Calendar.MONTH) + 1));
            return indices;
        }

        toCal.add(Calendar.MONTH, 1);
        toCal.add(Calendar.MILLISECOND, -1);

        long lastPointer = toCal.getTimeInMillis();
        long pointer = fromMillis;

        while (pointer < lastPointer) {
            int targetYear = fromCal.get(Calendar.YEAR);
            int targetMonth = fromCal.get(Calendar.MONTH) + 1;
            if (targetMonth == 0) targetMonth = 12;

            StringBuffer sbTarget = new StringBuffer(indexName);
            sbTarget.append("*").append(String.format("%4d-%02d", targetYear, targetMonth));
            indices.add(sbTarget.toString());

            fromCal.add(Calendar.MONTH, 1);
            pointer = fromCal.getTimeInMillis();
        }

        return indices;
    }

    /**
     * <pre>
     * Description :
     *     요청 관련 로깅
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
     * Date   : 2022. 4. 3.
     * </pre>
     */
    protected void appendRequestLog(StringBuffer sbLog, SearchRequest req, ElasticsearchClient client) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator generator = client._jsonpMapper().jsonProvider().createGenerator(baos);
        req.serialize(generator, client._jsonpMapper());
        List<String> indices = req.index();
        generator.close();
        try {
            final String jsonString = baos.toString("UTF-8");
            sbLog.append(String.format("%-10s: %s", "Indices", indices)).append(System.lineSeparator());
            sbLog.append(String.format("%-10s: \n%s", "Query", new ObjectMapper().readTree(jsonString).toPrettyString())).append(System.lineSeparator());
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            sbLog.append(String.format("%-10s: %s", "Indices", indices)).append(System.lineSeparator());
            sbLog.append(String.format("%-10s: %s", "Query", baos)).append(System.lineSeparator());
        }

    }

    /**
     * <pre>
     * Description :
     *     응답 관련 로깅
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
     * Date   : 2022. 4. 3.
     * </pre>
     */
    protected void appendResponseLog(StringBuffer sbLog, long total, long took) {
        sbLog.insert(0, String.format("%-10s: %d docs\n", "Found", total));
        sbLog.insert(0, String.format("%-10s: %d ms\n", "Took", took));
    }

}
