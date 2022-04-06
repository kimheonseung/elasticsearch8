package com.devh.example.elasticsearch8.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 * Description :
 *     sample-event 인덱스 관련 VO
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
@EqualsAndHashCode(callSuper = false)
@Builder
public class SampleEventVO extends AbstractLogVO {
	
	@JsonIgnore
	public final String indexName = "sample-event";
	
	private String who;
	private String when;
	private String where;
	private String what;
	private String how;
	private String why;
	
	@Override
	public String getIndexName() {
		return this.indexName;
	}
}
