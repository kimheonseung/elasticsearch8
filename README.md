## Elasticsearch8 Java API Example
  
### 1. Environment
- Java 8 (1.8_202)
- Elasticsearch 8.1.1 ([Docker](https://github.com/kimheonseung/elasticsearch8/tree/master/docker-es8))
- Gradle 7.2
- Springboot 2.5.3
  
### 2. Core Code
- [Service Login](https://github.com/kimheonseung/elasticsearch8/tree/master/src/main/java/com/devh/example/elasticsearch8/service)
- [API Common Code](https://github.com/kimheonseung/elasticsearch8/tree/master/src/main/java/com/devh/example/elasticsearch8/api)

### 3. API Test & Query log Monitoring
- http://localhost:8088/swagger-ui.html (API Spec)
- [Search, Chart Sample FE](https://github.com/kimheonseung/elasticsearch8/tree/master/react-app)

### 4. How to build & Notice
```shell
# Linux
chmod +x ./gradlew
./gradlew bootjar 
cd ./build/libs
java -jar elasticsearch8-v1.jar

# Windows
./graldew.bat bootjar
cd .\build\libs
java -jar elasticsearch8-v1.jar

# Unzip testlog.json.zip & locate the "testlog.json" file in where JAR file exists.
```
  
