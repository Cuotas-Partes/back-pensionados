# Basic Dockerfile for demonstration
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the pre-built JAR
COPY target/*.jar app.jar

# Expose port
EXPOSE 8080

# Set JVM options and run the application
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]