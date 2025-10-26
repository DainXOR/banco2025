# FROM openjdk:21-jdk-slim
# LABEL authors="DainXOR"
#
# EXPOSE 8080
# ADD target/banco_udea-0.0.1-SNAPSHOT.jar banco_udea-0.0.1-SNAPSHOT.jar
# ENTRYPOINT ["java", "-jar", "banco_udea-0.0.1-SNAPSHOT.jar"]

FROM maven:latest AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/banco_udea-0.0.1-SNAPSHOT.jar banco_udea-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "banco_udea-0.0.1-SNAPSHOT.jar"]