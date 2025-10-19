# ----------------------------------------
# Stage 1: Build the JAR using Maven
# ----------------------------------------
FROM eclipse-temurin:17-jdk-alpine AS builder

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
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy built JAR from the builder stage
COPY --from=builder /app/target/restaurantservice-0.0.1-SNAPSHOT.jar app.jar

# Expose service port
EXPOSE 8006

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

