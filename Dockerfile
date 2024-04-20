# 빌드 스테이지
FROM openjdk:17-slim as build

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 래퍼와 프로젝트 파일을 이미지로 복사합니다.
COPY gradlew .
COPY gradle gradle
RUN chmod +x ./gradlew
COPY build.gradle .
COPY settings.gradle .
COPY src src

# 애플리케이션 빌드
RUN ./gradlew bootJar --no-daemon

# 실행 스테이지
FROM openjdk:17-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 스테이지에서 생성된 실행 가능한 JAR 파일을 복사합니다.
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

# 컨테이너가 시작될 때 애플리케이션 실행
ENTRYPOINT ["java","-jar","app.jar"]
