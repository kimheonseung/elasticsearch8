package com.devh.example.elasticsearch8.service;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.devh.example.elasticsearch8.component.ESClientService;
import com.devh.example.elasticsearch8.exception.ESTemplateException;
import com.devh.example.elasticsearch8.util.JsonFileReader;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.cat.TemplatesRequest;
import co.elastic.clients.elasticsearch.cat.TemplatesResponse;
import co.elastic.clients.elasticsearch.cat.templates.TemplatesRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * Description :
 *     템플릿 관련 서비스 
 * ===============================================
 * Member fields :
 *     
 * ===============================================
 *
 * Author : HeonSeung Kim
 * Date   : 2022. 2. 18.
 * </pre>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ESTemplateService {
	
	private final ESClientService mESClientService;;
	
	/*
	 * <pre>
	 * Description : 
	 *     템플릿 유무를 확인하여 생성
	 * ===============================
	 * Parameters :
	 *     
	 * Returns :
	 *     
	 * Throws :
	 *     
	 * ===============================
	 * 
	 * Author : HeonSeung Kim
	 * Date   : 2022. 2. 18.
	 * </pre>
	 */
	public boolean updateTemplate() throws ESTemplateException {
		try {
			List<TemplatesRecord> sampleLogTemplates = searchTemplate("sample-log");
			if(sampleLogTemplates != null && sampleLogTemplates.size() > 0) {
				deleteTemplate(sampleLogTemplates);
			}
			createTemplate("sample-log");
			
			List<TemplatesRecord> sampleEventTemplates = searchTemplate("sample-event");
			if(sampleEventTemplates != null && sampleEventTemplates.size() > 0) {
				deleteTemplate(sampleEventTemplates);
			}
			createTemplate("sample-event");	
			
			return true;
		} catch (Exception e) {
			throw new ESTemplateException(e.getMessage());
		}
		
	}
	
	/**
	 * <pre>
	 * Description :
	 *     템플릿 검색
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
	private List<TemplatesRecord> searchTemplate(String templateName) throws Exception {
		try {
			TemplatesRequest templateRequest = new TemplatesRequest.Builder().name(templateName).build();
			TemplatesResponse templateResponse = mESClientService.getElasticsearchClient().cat().templates(templateRequest);
			return templateResponse.valueBody();
		} catch (ElasticsearchException e) {
			log.warn(e.getMessage());
			throw e;
		} catch (IOException e) {
			log.warn(e.getMessage());
			throw e;
		}
	}
	
	/**
	 * <pre>
	 * Description :
	 *     템플릿 삭제
	 *     low level 요청 수행
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
	private void deleteTemplate(List<TemplatesRecord> templates) {
		templates.forEach(template -> {
			Request request = new Request("DELETE", "_template/"+template.name());
			try {
				Response response = mESClientService.getRestClient().performRequest(request);
				log.info(String.format("[%6s] %s: %d", request.getMethod(), request.getEndpoint(), response.getStatusLine().getStatusCode()));
			} catch (IOException e) {
				log.warn(e.getMessage());
			}
		});
		
	}
	
	/**
	 * <pre>
	 * Description :
	 *     json파일 기준으로 템플릿 생성
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
	@SuppressWarnings("unchecked")
	private void createTemplate(String templateName) throws Exception {
		try {
//			JSONArray jsonArray = JsonFileReader.readJsonFileToJSONArray(getClass().getClassLoader().getResource("templates.json").getPath());
			JSONArray jsonArray = JsonFileReader.readJsonFileToJSONArray(getClass().getClassLoader().getResourceAsStream("templates.json"));
			jsonArray.forEach(jsonItem -> {
				JSONObject json = (JSONObject) jsonItem;
				if(json.containsKey(templateName)) {
					JSONObject templateJson = (JSONObject) json.get(templateName);
					Request request = new Request("PUT", "_template/"+templateName);
					request.setJsonEntity(templateJson.toJSONString());
					try {
						Response response = mESClientService.getRestClient().performRequest(request);
						log.info(String.format("[%6s] %s: %d", request.getMethod(), request.getEndpoint(), response.getStatusLine().getStatusCode()));
					} catch (IOException e) {
						log.warn(e.getMessage());
					}
				}
			});
		} catch (Exception e) {
			log.warn(e.getMessage());
			throw e;
		}
	}
}
