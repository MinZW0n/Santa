# 빌드 스테이지
FROM openjdk:17-slim as build

# 작업 디렉토리 설정
WORKDIR /app

# 최소한의 파일만 먼저 복사하여 빌드 캐시 활용을 극대화함
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# gradlew 실행 권한 부여 및 의존성 다운로드
RUN chmod +x ./gradlew && ./gradlew --version

# 나머지 프로젝트 파일을 복사
COPY src src

# 애플리케이션이 실제로 필요한 구성 요소만 다시 빌드
RUN ./gradlew bootJar --no-daemon

# 실행 스테이지
FROM openjdk:17-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 스테이지에서 생성된 실행 가능한 JAR 파일을 복사
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

# 컨테이너가 시작될 때 애플리케이션 실행
ENTRYPOINT ["java","-jar","app.jar"]
