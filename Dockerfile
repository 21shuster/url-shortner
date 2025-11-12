# --- FASE 1 : BUILD ---
FROM gradle:8.7-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# Vars for Spring Boot
ENV MONGO_USER=alejandrogarcb21_db_user
ENV MONGO_PASSWORD=9QG5M13QJEvtqOU0
ENV MONGO_CLUSTER=cluster0.jpmbeze.mongodb.net
ENV MONGO_DB=urlshortenerdb

# --- FASE 2 : RUNTIME ---
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
