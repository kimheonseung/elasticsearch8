## Elasticsearch8 Java API Example
  
### 1. 환경
- Java 8 (1.8_202)
- Elasticsearch 8.1.1 ([Docker](https://github.com/kimheonseung/elasticsearch8/tree/master/docker-es8))
- Gradle 7.2
- Springboot 2.5.3
  
### 2. 주요 코드
- [서비스 로직](https://github.com/kimheonseung/elasticsearch8/tree/master/src/main/java/com/devh/example/elasticsearch8/service)
- [API 공통코드](https://github.com/kimheonseung/elasticsearch8/tree/master/src/main/java/com/devh/example/elasticsearch8/api)

### 3. API 테스트 및 쿼리 로그 모니터링
- http://localhost:8088/swagger-ui.html (구동 후 API Spec 확인)
- [검색 및 차트 화면 샘플](https://github.com/kimheonseung/elasticsearch8/tree/master/react-app)

### 4. 빌드와 구동 및 주의사항
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

# testlog.json.zip 압축해제 후 testlog.json 파일을 jar 파일과 동일한 경로에 위치
```
  
