# ---- FASE 1: build ----
FROM gradle:8.7-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# ---- FASE 2: runtime ----
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
