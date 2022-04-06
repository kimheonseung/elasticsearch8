package com.devh.example.elasticsearch8.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * Description :
 *     색인할 로그 공통 필드
 * ===============================================
 * Member fields :
 *     
 * ===============================================
 *
 * Author : HeonSeung Kim
 * Date   : 2022. 4. 3.
 * </pre>
 */
@Getter
@Setter
public abstract class AbstractLogVO {
	private long timeMillis;
	private String originalLog;
	@JsonIgnore
	private LogType logType;
	@JsonIgnore
	private ReadingSignal readingSignal;
	
	public abstract String getIndexName(); 
	
	public static enum LogType {
		EVENT, LOG;
	}
	
	public static enum ReadingSignal {
		READING, END;
	}
}
