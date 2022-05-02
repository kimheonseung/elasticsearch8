package com.devh.example.elasticsearch8.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <pre>
 * Description :
 *     sample-log 인덱스 관련 VO
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SampleLogVO extends AbstractLogVO {
	
	@JsonIgnore
	public final String indexName = "sample-log";
	@JsonIgnore
	public static final String IP = "ip";
	@JsonIgnore
	public static final String EQUIP_NAME = "equipName";
	@JsonIgnore
	public static final String LOG_PATH = "logPath";
	@JsonIgnore
	public static final String LOG_NAME = "logName";
	@JsonIgnore
	public static final String LOG = "log";
	@JsonIgnore
	public static final String ORIGINAL_LOG = "originalLog";
	
	
	private String ip;
	private String equipName;
	private String logPath;
	private String logName;
	private String log;
	private String originalLog;
	
	@Override
	public String getIndexName() {
		return this.indexName;
	}
}
