package com.devh.example.elasticsearch8.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import com.devh.example.elasticsearch8.component.ESClientService;
import com.devh.example.elasticsearch8.component.QueueManager;
import com.devh.example.elasticsearch8.exception.ESIndexException;
import com.devh.example.elasticsearch8.test.SampleEventProducer;
import com.devh.example.elasticsearch8.test.SampleLogProducer;
import com.devh.example.elasticsearch8.util.ExceptionUtils;
import com.devh.example.elasticsearch8.vo.AbstractLogVO;
import com.devh.example.elasticsearch8.vo.AbstractLogVO.ReadingSignal;
import com.devh.example.elasticsearch8.vo.IndexVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * <pre>
 * Description :
 *     인덱스 관련 작업 서비스
 *     로그생성, 인덱스 목록 조회, 인덱스 삭제 등
 * ===============================================
 * Member fields :
 *     
 * ===============================================
 *
 * Author : HeonSeung Kim
 * Date   : 2022. 4. 3.
 * </pre>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ESIndexService {
	
	private final ESClientService mESClientService;
	private final QueueManager queueManager;

	Thread esIndexConsumerThread;
	Thread sampleLogProducerThread;
	Thread sampleEventProducerThread;
	
	/**
	 * <pre>
	 * Description :
	 *     sample-log 생성 스레드 시작
	 * ===============================================
	 * Parameters :
	 *     
	 * Returns :
	 *     
	 * Throws :
	 *     
	 * ===============================================
	 *
	 * Author : HeonSeung Kim
	 * Date   : 2022. 4. 3.
	 * </pre>
	 */
	public void loadSampleLog() {
		if(sampleLogProducerThread == null) {
			sampleLogProducerThread = new Thread(new SampleLogProducer(queueManager.getLogQueue()), "SampleLogProducer");
			sampleLogProducerThread.start();
		} else if(!sampleLogProducerThread.isAlive()) {
			sampleLogProducerThread.start();
		}
	}
	
	/**
	 * <pre>
	 * Description :
	 *     sample-event 생성 스레드 시작
	 * ===============================================
	 * Parameters :
	 *     
	 * Returns :
	 *     
	 * Throws :
	 *     
	 * ===============================================
	 *
	 * Author : HeonSeung Kim
	 * Date   : 2022. 4. 3.
	 * </pre>
	 */
	public void loadSampleEvent() {
		if(sampleEventProducerThread == null) {
			sampleEventProducerThread = new Thread(new SampleEventProducer(queueManager.getLogQueue()), "SampleEventProducer");
			sampleEventProducerThread.start();
		} else if(!sampleEventProducerThread.isAlive()) {
			sampleEventProducerThread.start();
		}
	}
	
	/**
	 * <pre>
	 * Description :
	 *     sample-log 생성 스레드 시작
	 * ===============================================
	 * Parameters :
	 *     
	 * Returns :
	 *     
	 * Throws :
	 *     
	 * ===============================================
	 *
	 * Author : HeonSeung Kim
	 * Date   : 2022. 4. 3.
	 * </pre>
	 */
	public void startESIndexConsumerThread() {
		if(esIndexConsumerThread == null) {
			esIndexConsumerThread = new Thread(new ESIndexConsumer(queueManager.getLogQueue()), "ESIndexConsumer");
			esIndexConsumerThread.start();
		} else if(!esIndexConsumerThread.isAlive())
			esIndexConsumerThread.start();
	}
	
	/**
	 * <pre>
	 * Description :
	 *     인덱스 목록 및 상태 정보 조회
	 * ===============================================
	 * Parameters :
	 *     
	 * Returns :
	 *     
	 * Throws :
	 *     
	 * ===============================================
	 *
	 * Author : HeonSeung Kim
	 * Date   : 2022. 4. 3.
	 * </pre>
	 */
	public List<IndexVO> getIndices() throws ESIndexException {
		try {
			List<IndexVO> indices = new ArrayList<>();
			
			mESClientService.getElasticsearchClient()
				.cat()
				.indices()
				.valueBody().forEach(indicesRecord -> {
					try {
						indices.add(IndexVO.builder()
								.health(indicesRecord.health())
								.status(indicesRecord.status())
								.name(indicesRecord.index())
								.uuid(indicesRecord.uuid())
								.pri(indicesRecord.pri())
								.rep(indicesRecord.rep())
								.docsCount(indicesRecord.docsCount())
								.storeSize(indicesRecord.storeSize())
								.priStoreSize(indicesRecord.priStoreSize())
								.build());
					} catch (Exception e) {
						log.error(ExceptionUtils.stackTraceToString(e));
					}
				});
			
			return indices;
		} catch (Exception e) {
			throw new ESIndexException(e.getMessage());
		}
	}
	
	/**
	 * <pre>
	 * Description :
	 *     sample 인덱스 삭제
	 * ===============================================
	 * Parameters :
	 *     
	 * Returns :
	 *     
	 * Throws :
	 *     
	 * ===============================================
	 *
	 * Author : HeonSeung Kim
	 * Date   : 2022. 4. 3.
	 * </pre>
	 */
	public boolean deleteSample() throws ESIndexException {
		try {
			
			boolean result = false;
			
			List<String> indexList = new ArrayList<>();
			
			mESClientService.getElasticsearchClient()
				.cat()
				.indices()
				.valueBody().forEach(indicesRecord -> {
					final String index = indicesRecord.index();
					if(index != null && "sample-".startsWith(index))
						indexList.add(index);
				});
			
			if(indexList.size() > 0) {
				DeleteIndexResponse res = mESClientService.getElasticsearchClient()
						.indices()
						.delete(d -> d
								.index(indexList)
								);
				
				result = res.acknowledged();
				log.info(String.format("Indices: %s, result: %b", indexList, result));
			}
			
			return result;
		} catch (Exception e) {
			throw new ESIndexException(e.getMessage());
		}
	}
	
	/**
	 * <pre>
	 * Description :
	 *     Producer들이 생성한 로그를 색인하는 Consumer 클래스
	 * ===============================================
	 * Member fields :
	 *     
	 * ===============================================
	 *
	 * Author : HeonSeung Kim
	 * Date   : 2022. 4. 3.
	 * </pre>
	 */
	private class ESIndexConsumer implements Runnable {

		private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		private final BlockingQueue<AbstractLogVO> logQueue;
		private int count = 0;
		
		public ESIndexConsumer(BlockingQueue<AbstractLogVO> logQueue) {
			this.logQueue = logQueue;
		}

		private AbstractLogVO dequeue() {
			AbstractLogVO result = null;
			try {
				result = this.logQueue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return result;
		}
		
		@Override
		public void run() {
			try {
				
				final List<BulkOperation> bulkOperationList = new ArrayList<>();
				
				while(!Thread.currentThread().isInterrupted()) {
					
					++count;
					AbstractLogVO abstractLogVO = dequeue();
					final ReadingSignal readingSignal = abstractLogVO.getReadingSignal();
					final String indexName = generateIndexName(abstractLogVO);
					switch (readingSignal) {
					case READING:
						bulkOperationList.add(
							BulkOperation.of(b -> b
								.index(
									IndexOperation.of(i -> i
										.index(indexName)
										.document(abstractLogVO)
									)
								)
							)
						);
						bulkWhileReading(bulkOperationList);
						break;
					case END:
						bulkWhileEnd(bulkOperationList);
						break;

					}
					
				}
				
			} catch (Exception e) {
				log.error(ExceptionUtils.stackTraceToString(e));
			}
			
		}
		
		/**
		 * <pre>
		 * Description :
		 *     로그 기준 시간으로 인덱스명 생성
		 * ===============================================
		 * Parameters :
		 *     
		 * Returns :
		 *     
		 * Throws :
		 *     
		 * ===============================================
		 *
		 * Author : HeonSeung Kim
		 * Date   : 2022. 4. 3.
		 * </pre>
		 */
		private String generateIndexName(AbstractLogVO abstractLogVO) {
			String indexName;
			try {
				final String indexPrefix = abstractLogVO.getIndexName();
				final long timeMillis = abstractLogVO.getTimeMillis();
				indexName = String.format("%s_%s", indexPrefix, sdf.format(timeMillis));
			} catch (Exception e) {
				e.printStackTrace();
				indexName = String.format("debug_%s", sdf.format(System.currentTimeMillis()));
			}
			return indexName;
		}
		
		/**
		 * <pre>
		 * Description :
		 *     벌크 요청 커밋
		 * ===============================================
		 * Parameters :
		 *     
		 * Returns :
		 *     
		 * Throws :
		 *     
		 * ===============================================
		 *
		 * Author : HeonSeung Kim
		 * Date   : 2022. 4. 3.
		 * </pre>
		 */
		private void commitBulk(List<BulkOperation> bulkOperationList) {
			try {
				ElasticsearchClient elasticsearchClient = mESClientService.getElasticsearchClient();
				BulkResponse bulkResponse = elasticsearchClient.bulk(
					BulkRequest.of(b -> b
						.operations(bulkOperationList)
					)
				);
				if(bulkResponse.errors()) {
					bulkResponse.items().forEach(i -> log.warn(String.format("Error: %s\nDocument: %s", i.error(), i.get())));
					
				} else {
					log.info("Bulk commit. took: " + bulkResponse.took() + "ms, count: " + count);
					count = 0;
				}
			} catch (Exception e) {
				log.error(ExceptionUtils.stackTraceToString(e));
			}
		}
		
		/**
		 * <pre>
		 * Description :
		 *     벌크 단위만큼 쌓인 요청을 벌크 시도
		 * ===============================================
		 * Parameters :
		 *     
		 * Returns :
		 *     
		 * Throws :
		 *     
		 * ===============================================
		 *
		 * Author : HeonSeung Kim
		 * Date   : 2022. 4. 3.
		 * </pre>
		 */
		private void bulkWhileReading(List<BulkOperation> bulkOperationList) {
			if(count > 0 && (count % 1500) == 0) {
				commitBulk(bulkOperationList);
				bulkOperationList.clear();
			}
		}
		
		/**
		 * <pre>
		 * Description :
		 *     파일 읽기가 끝나 쌓인 요청을 벌크 시도
		 * ===============================================
		 * Parameters :
		 *     
		 * Returns :
		 *     
		 * Throws :
		 *     
		 * ===============================================
		 *
		 * Author : HeonSeung Kim
		 * Date   : 2022. 4. 3.
		 * </pre>
		 */
		private void bulkWhileEnd(List<BulkOperation> bulkOperationList) {
			if(count > 0) {
				commitBulk(bulkOperationList);
				bulkOperationList.clear();
				count = 0;
			}
		}
		
	}
}
