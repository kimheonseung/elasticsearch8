package com.devh.example.elasticsearch8.vo;

import lombok.Builder;
import lombok.Data;

/**
 * <pre>
 * Description :
 *     인덱스 목록 조회 관련 VO
 * ===============================================
 * Member fields :
 *     
 * ===============================================
 *
 * Author : HeonSeung Kim
 * Date   : 2022. 4. 3.
 * </pre>
 */
@Builder
@Data
public class IndexVO {
	private String health;
	private String status;
	private String name;
	private String uuid;
	private String pri;
	private String rep;
	private String docsCount;
	private String storeSize;
	private String priStoreSize;
}
