# OpenJDK 17 이미지 사용
FROM openjdk:17-jdk-alpine

# JAR 파일을 컨테이너 안으로 복사
COPY build/libs/your-project-name-0.0.1-SNAPSHOT.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]