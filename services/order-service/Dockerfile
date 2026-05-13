FROM gradle:9.4.1-jdk21-ubi AS build

WORKDIR /build

# Root project files needed for the version catalog and multi-project settings
COPY settings.gradle settings.gradle
COPY build.gradle build.gradle
COPY gradle gradle

# Both service build files must exist so Gradle can resolve the project graph
COPY services/auth-service/build.gradle services/auth-service/build.gradle
COPY services/order-service/build.gradle services/order-service/build.gradle

# Resolve and cache dependencies before copying source
RUN gradle --no-daemon :services:order-service:dependencies

COPY services/order-service/src services/order-service/src

RUN gradle --no-daemon :services:order-service:build -x test

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /build/services/order-service/build/libs/order-service-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
