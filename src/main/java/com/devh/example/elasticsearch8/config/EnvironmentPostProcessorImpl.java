package com.devh.example.elasticsearch8.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * <pre>
 * Description :
 *     SpringBoot가 구동되기 전에, 특정 파일을 읽어
 *     그 파일의 key, value 쌍을 application.properties에서 사용하기 위해
 *     먼저 파일을 load하는 클래스.
 *     이 클래스가 SpringBoot 구동 시 작동하게 하려면,
 *     resources/META-INF/spring.factories 파일에 해당 클래스를 등록해야함
 * ===============================
 * Memberfields :
 *     
 * ===============================
 * 
 * Author : HeonSeung Kim
 * Date   : 2021. 12. 20.
 * </pre>
 */
public class EnvironmentPostProcessorImpl implements EnvironmentPostProcessor {

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

        try {
            /* 다음 파일을 읽어 customProp 이라는 별칭으로 key, value 쌍 등록 */
            File customProp = new File("config.properties");

            try (InputStream inputStream = new FileInputStream(customProp)) {
                Properties properties = new Properties();
                properties.load(inputStream);

                environment.getPropertySources().addFirst(new PropertiesPropertySource("customProps", properties));

                System.out.println("Success to load " + customProp.getAbsolutePath());
            } catch (Exception e) {
                System.out.println("Skip Custom Properties.");
            }

        } catch (Exception e) {
            System.out.println("Skip Custom Properties.");
        }
	}

}