## Elasticsearch8 Java API Example
  
### 1. 환경
- Java 8 (1.8_202)
- Elasticsearch 8.1.1 ([Docker](https://github.com/kimheonseung/elasticsearch8/tree/master/docker-es8))
- Gradle 7.2
- Springboot 2.5.3
  
### 2. 주요 코드
- [서비스 로직](https://github.com/kimheonseung/elasticsearch8/tree/master/src/main/java/com/devh/example/elasticsearch8/service)
- [API 공통코드](https://github.com/kimheonseung/elasticsearch8/tree/master/src/main/java/com/devh/example/elasticsearch8/api)
  
### 3. 빌드
```shell
# Linux
chomd +x ./gradlew
./gradlew bootjar 
cd ./build/libs
java -jar elasticsearch8-v1.jar

# Windows
./graldew.bat bootjar
cd .\build\libs
java -jar elasticsearch8-v1.jar
```
  
### API 테스트 및 쿼리 로그 모니터링
```shell
# Swagger 라이브러리로 작성된 API 명세서
http://localhost:8088/swagger-ui.html

# 각 API 요청 후 프로그램 로그에 쿼리 로깅
```

### 샘플 데이터 생성 시 주의점
- jar 파일과 동일한 경로에 testlog.json 파일이 있어야함