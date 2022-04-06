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
import com.devh.example.elasticsearch8.vo.SampleLogVO;

/**
 * <pre>
 * Description :
 *     sample-log 로그 생성기
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
public class SampleLogProducer implements Runnable {

	private final BlockingQueue<AbstractLogVO> mQueue;
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
	
	private final String[] ips = new String[] {
		"192.168.100.1",
		"192.168.100.2",
		"192.168.100.3",
		"192.168.100.4",
		"192.168.100.5",
		"192.168.100.6",
		"192.168.100.7",
		"192.168.100.8",
		"192.168.100.9",
		"192.168.100.10",
		"192.168.100.11",
		"192.168.100.12",
		"192.168.100.13",
		"192.168.100.14",
		"192.168.100.15",
		"192.168.100.16",
		"192.168.100.17",
		"192.168.100.18",
		"192.168.100.19",
		"192.168.100.20",
	};
	
	public SampleLogProducer(BlockingQueue<AbstractLogVO> queue) {
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
				final int idx = (int) (Math.random() * 20);
				try {
					JSONObject logJson = (JSONObject) json;
					AbstractLogVO abstractLogVO = SampleLogVO.builder()
							.ip(this.ips[idx])
							.equipName(String.valueOf(logJson.get("deviceProduct")))
							.logPath(String.valueOf(logJson.get("deviceVendor"))+File.separator+String.valueOf(logJson.get("signatureId")))
							.logName(String.valueOf(logJson.get("name")))
							.log(String.valueOf(logJson.get("message")))
							.build();
					abstractLogVO.setLogType(LogType.LOG);
					abstractLogVO.setOriginalLog(logJson.toJSONString());
					abstractLogVO.setReadingSignal(ReadingSignal.READING);
					abstractLogVO.setTimeMillis(sdf.parse(String.valueOf(logJson.get("eventStartTime"))).getTime());
					enqueue(abstractLogVO);
				} catch (Exception e) {
//					e.printStackTrace();
				}
				
			});
			
			AbstractLogVO abstractLogVO = SampleLogVO.builder().build();
			abstractLogVO.setLogType(LogType.LOG);
			abstractLogVO.setReadingSignal(ReadingSignal.END);
			enqueue(abstractLogVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
