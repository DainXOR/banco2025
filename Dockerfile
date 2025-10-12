FROM openjdk:21-jdk-slim
LABEL authors="DainXOR"

EXPOSE 8080
ADD target/banco_udea.jar banco_udea-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "banco_udea-0.0.1-SNAPSHOT.jar"]