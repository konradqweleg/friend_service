
FROM gradle:8.3-jdk17 AS build

WORKDIR /app

COPY build.gradle settings.gradle /app/
COPY src /app/src

RUN gradle clean bootJar




FROM openjdk:17-jdk-slim AS runtime

WORKDIR /app

COPY --from=build /app/build/libs/mychat-friends-service.jar mychat-friends-service.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "mychat-friends-service.jar"]
