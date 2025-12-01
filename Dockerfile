# --------------------------< Lab 1 >-------------------------- #

# FROM openjdk:21-jdk-slim
# LABEL authors="DainXOR"
#
# EXPOSE 8080
# ADD target/banco_udea-0.0.1-SNAPSHOT.jar banco_udea-0.0.1-SNAPSHOT.jar
# ENTRYPOINT ["java", "-jar", "banco_udea-0.0.1-SNAPSHOT.jar"]

# --------------------------< Lab 2 >-------------------------- #

# FROM maven:latest AS build
# WORKDIR /app
# COPY . .
# RUN mvn clean package -DskipTests
#
# FROM openjdk:26-jdk-slim
# WORKDIR /app
#
# # Copy the built jar from the build stage
# COPY --from=build /app/target/banco_udea-0.0.1-SNAPSHOT.jar banco_udea-0.0.1-SNAPSHOT.jar
# EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "banco_udea-0.0.1-SNAPSHOT.jar"]

# --------------------------< Lab 3 >-------------------------- #

# ---------- BUILD STAGE ---------- #
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only pom.xml first for better caching
COPY pom.xml .
RUN mvn -e -B dependency:go-offline

# Copy source code
COPY src ./src

# Build the JAR
RUN mvn -e -B clean package -DskipTests

# ---------- RUNTIME STAGE ---------- #
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the JAR from previous stage
COPY --from=build /app/target/banco_udea-0.0.1-SNAPSHOT.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
