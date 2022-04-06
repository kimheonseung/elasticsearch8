package com.devh.example.elasticsearch8.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <pre>
 * Description :
 *     sample-log 인덱스 검색 관련 객체  
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
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class SampleLogSearchVO extends SearchCommonVO {
	public SampleLogSearchVO() { super(); }
	private String[] ip = new String[] {};
	private String[] equipName = new String[] {};
	private String[] logPath = new String[] {};
	private String[] logName = new String[] {};
	private String[] log = new String[] {};
	
	private String[] ipOperator = new String[] {};
	private String[] equipNameOperator = new String[] {};
	private String[] logPathOperator = new String[] {};
	private String[] logNameOperator = new String[] {};
	private String[] logOperator = new String[] {};
}
