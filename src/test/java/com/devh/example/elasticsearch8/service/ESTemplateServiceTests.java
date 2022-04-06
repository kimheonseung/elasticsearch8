package com.devh.example.elasticsearch8.service;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import com.devh.example.elasticsearch8.component.ESClientService;
import com.devh.example.elasticsearch8.exception.ESTemplateException;

public class ESTemplateServiceTests {

	@Test
	public void test_updateTemplate() throws ESTemplateException {
		ESClientService s = new ESClientService();
		ESTemplateService t = new ESTemplateService(s);
		assertTrue(t.updateTemplate());
	}
}
