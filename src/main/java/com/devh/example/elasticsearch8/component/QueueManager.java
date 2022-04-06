package com.devh.example.elasticsearch8.component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.stereotype.Component;

import com.devh.example.elasticsearch8.vo.AbstractLogVO;

import lombok.Getter;

/**
 * <pre>
 * Description :
 *     시스템 곳곳에서 사용할 큐를 관리하는 객체
 * ===============================================
 * Member fields :
 *     
 * ===============================================
 *
 * Author : HeonSeung Kim
 * Date   : 2022. 4. 4.
 * </pre>
 */
@Component
@Getter
public class QueueManager {
	private final BlockingQueue<AbstractLogVO> logQueue;
	
	public QueueManager() {
		this.logQueue = new ArrayBlockingQueue<AbstractLogVO>(5000, true);
	}
}
