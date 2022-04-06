package com.devh.example.elasticsearch8.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * <pre>
 * Description :
 *     검색관련 공통 변수 객체
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
@ToString
public abstract class SearchCommonVO {
	public SearchCommonVO() {};
	private long fromMillis;
	private long toMillis = System.currentTimeMillis();
	private int page = 1;
	private int pageSize = 10;
	private int rows = 10;
	private String sortIndex = "timeMillis";
	private String sortOrder = "Desc";
	private boolean aggregation = false;
	private String[] aggregationType = new String[] { "term" };
	private String[] aggregationField = new String[] {};
	private int[] aggregationTopN = new int[] { 10 };
	private String[] aggregationInterval = new String[] {};
}
