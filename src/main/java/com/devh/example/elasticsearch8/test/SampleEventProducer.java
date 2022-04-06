package com.devh.example.elasticsearch8.test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.concurrent.BlockingQueue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.devh.example.elasticsearch8.util.JsonFileReader;
import com.devh.example.elasticsearch8.vo.AbstractLogVO;
import com.devh.example.elasticsearch8.vo.AbstractLogVO.LogType;
import com.devh.example.elasticsearch8.vo.AbstractLogVO.ReadingSignal;
import com.devh.example.elasticsearch8.vo.SampleEventVO;
import com.devh.example.elasticsearch8.vo.SampleLogVO;

/**
 * <pre>
 * Description :
 *     sample-event 로그 생성기
 *     객체를 생성하여 인덱스 스레드로 객체 전달  
 * ===============================================
 * Member fields :
 *     
 * ===============================================
 *
 * Author : HeonSeung Kim
 * Date   : 2022. 4. 3.
 * </pre>
 */
public class SampleEventProducer implements Runnable {

	private final BlockingQueue<AbstractLogVO> mQueue;
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
	
	public SampleEventProducer(BlockingQueue<AbstractLogVO> queue) {
		this.mQueue = queue;
	}
	
	private void enqueue(AbstractLogVO abstractLogVO) {
		try {
			this.mQueue.put(abstractLogVO);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		File file = new File("testlog.json");
		try {
			JSONArray jsonArray = JsonFileReader.readJsonFileToJSONArray(file.getAbsolutePath());
			jsonArray.forEach(json -> {
				try {
					JSONObject logJson = (JSONObject) json;
					AbstractLogVO abstractLogVO = SampleEventVO.builder()
							.who(String.valueOf(logJson.get("deviceVendor")))
							.when(String.valueOf(logJson.get("eventStartTime")))
							.where(String.valueOf(logJson.get("deviceProduct")))
							.what(String.valueOf(logJson.get("name")))
							.how(String.valueOf(logJson.get("signatureId")))
							.why(String.valueOf(logJson.get("message")))
							.build();
					abstractLogVO.setLogType(LogType.EVENT);
					abstractLogVO.setOriginalLog(logJson.toJSONString());
					abstractLogVO.setReadingSignal(ReadingSignal.READING);
					abstractLogVO.setTimeMillis(sdf.parse(String.valueOf(logJson.get("eventStartTime"))).getTime());
					enqueue(abstractLogVO);
				} catch (Exception e) {
//					e.printStackTrace();
				}
				
			});
			
			AbstractLogVO abstractLogVO = SampleLogVO.builder().build();
			abstractLogVO.setLogType(LogType.EVENT);
			abstractLogVO.setReadingSignal(ReadingSignal.END);
			enqueue(abstractLogVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
