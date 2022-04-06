package com.devh.example.elasticsearch8.vo;

import java.util.List;
import java.util.Map;

import com.devh.example.elasticsearch8.api.vo.PagingVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <pre>
 * Description :
 *     검색 및 집계 결과를 담는 VO
 *     검색이나 집계에 따라 세팅되지 않는 필드들이 있을 수 있음
 * ===============================================
 * Member fields :
 *     
 * ===============================================
 *
 * Author : HeonSeung Kim
 * Date   : 2022. 4. 3.
 * </pre>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultVO<T> {
	private List<T> list;
	private long total;
	private PagingVO paging;
	
	private boolean aggregation;
	private Map<String, List<AggregationVO>> aggregationMap;
	
	@Getter
	@Setter
	public static class AggregationVO {
		private String key;
		private long docCount;
	}
}
