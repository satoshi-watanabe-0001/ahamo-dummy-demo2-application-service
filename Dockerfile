FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy gradle wrapper and build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copy source code
COPY src src

# Make gradlew executable
RUN chmod +x ./gradlew

# Build the application
RUN ./gradlew build -x test

# Expose port
EXPOSE 8083

# Run the application
CMD ["java", "-jar", "build/libs/ahamo-dummy-demo2-application-service-1.0.0-SNAPSHOT.jar"]
