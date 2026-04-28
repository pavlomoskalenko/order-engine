FROM gradle:9.4.1-jdk21-ubi AS build

WORKDIR /build

COPY build.gradle settings.gradle ./
COPY gradle ./gradle

RUN gradle --no-daemon dependencies

COPY src ./src

RUN gradle --no-daemon build

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /build/build/libs/order-system-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]