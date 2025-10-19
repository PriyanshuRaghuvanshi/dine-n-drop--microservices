# ----------------------------------------
# Stage 1: Build the JAR using Maven
# ----------------------------------------
FROM openjdk:17-alpine AS builder

WORKDIR /app

# Copy pom.xml and download dependencies (caching optimization)
COPY pom.xml .
RUN apk add --no-cache maven
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the Spring Boot JAR
RUN mvn clean package -DskipTests

# ----------------------------------------
# Stage 2: Run the JAR
# ----------------------------------------
FROM openjdk:17-alpine

WORKDIR /app

# Copy built JAR from the builder stage
COPY --from=builder /app/target/auth-service-0.0.1-SNAPSHOT.jar app.jar

# Expose service port
EXPOSE 8002

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
