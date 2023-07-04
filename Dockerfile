FROM gradle:7.6.1-jdk17 AS build
# 전체 소스 코드 복사
COPY . /home/gradle/src
WORKDIR /home/gradle/src
# Gradle 빌드 실행 (api 모듈 빌드에 필요한 모든 의존성과 함께)
RUN gradle api:build

FROM mcr.microsoft.com/java/jre:17-zulu-ubuntu
# 빌드 스테이지에서 생성된 API 모듈의 JAR 파일을 이미지에 복사
COPY --from=build /home/gradle/src/api/build/libs/*.jar /app.jar

# 8080 포트 노출
EXPOSE 8080
# Java 애플리케이션 실행 (JVM에 타임존 설정 추가)
ENTRYPOINT ["sh", "-c", "java -Duser.timezone=Asia/Seoul ${JAVA_OPTS} ${JAVA_ACTIVE} -jar /app.jar"]
