# ----------------------------------------
# Stage 1: Build the JAR using Maven
# ----------------------------------------
FROM openjdk:17 AS builder

WORKDIR /app


# Copy built JAR from the builder stage
COPY target/restaurantservice-0.0.1-SNAPSHOT.jar app.jar

# Expose service port
EXPOSE 8006

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

