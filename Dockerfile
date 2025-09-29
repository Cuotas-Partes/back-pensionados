FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app_pensionados_back.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_pensionados_back.jar"]

# FROM openjdk:21-jdk
# ARG JAR_FILE=./target/back-pensionados-0.0.1-SNAPSHOT.jar
# COPY ${JAR_FILE} app_pensionados_back.jar
# EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "app_pensionados_back.jar"]