package com.devh.example.elasticsearch8.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.devh.example.elasticsearch8.exception.ESSearchException;
import com.devh.example.elasticsearch8.vo.SampleLogSearchVO;

@SpringBootTest
public class ESSearchServiceTests {
	
	@Autowired
	private ESSampleLogSearchService esSearchService;
	
	@Test
	public void test_search() throws ESSearchException {
		SampleLogSearchVO sampleLogSearchVO = new SampleLogSearchVO();
		sampleLogSearchVO.setFromMillis(1647793703000L);
		sampleLogSearchVO.setToMillis(1648916511000L);
		sampleLogSearchVO.setPage(3);
		sampleLogSearchVO.setRows(10);
		sampleLogSearchVO.setSortIndex("timeMillis");
		sampleLogSearchVO.setSortOrder("Desc");
		sampleLogSearchVO.setIp(new String[] {"192.168.100.1", "192.168.100.2"});
		sampleLogSearchVO.setIpOperator(new String[] {"eq", "eq"});
		
		esSearchService.search(sampleLogSearchVO)
		.getList()
		.forEach(vo -> {
			System.out.println(vo);
		});
	}
}
