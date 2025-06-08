# Build stage
FROM maven:3.8.6-openjdk17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY livestock-api/pom.xml livestock-api/
COPY livestock-common/pom.xml livestock-common/
COPY livestock-api-client/pom.xml livestock-api-client/
COPY livestock-core/pom.xml livestock-core/
COPY livestock-persistence/pom.xml livestock-persistence/
COPY livestock-app/pom.xml livestock-app/
RUN mvn -B dependency:go-offline
COPY . .
RUN mvn -B clean package -pl livestock-app -am -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-jammy
RUN apt-get update && apt-get install -y libjemalloc2 && rm -rf /var/lib/apt/lists/*
ENV LD_PRELOAD=/usr/lib/x86_64-linux-gnu/libjemalloc.so.2
ENV MALLOC_CONF=dirty_decay_ms:1000,narenas:2,background_thread:true
WORKDIR /app
COPY --from=build /app/livestock-app/target/livestock-app-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAM=350m", "-jar", "app.jar"]