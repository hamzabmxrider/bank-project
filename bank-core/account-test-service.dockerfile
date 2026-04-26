# Use OpenJDK 17
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

ARG APPLICATION_NAME=bank-core
# Copy built JAR
ARG JAR_FILE=target/bank-core-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Expose port
EXPOSE 8080

# Environment variables (can override at runtime)
ENV JAVA_OPTS="-Xms512m -Xmx1024m"
ENV SPRING_PROFILES_ACTIVE=production

# Volume for logs or other persistent files
VOLUME ["/app/logs"]

# Run Spring Boot app
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]

