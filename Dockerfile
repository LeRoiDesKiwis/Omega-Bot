FROM --platform=$BUILDPLATFORM amazoncorretto:21 AS build-stage
LABEL authors="leroideskiwis"

WORKDIR /builder/
COPY . .

# Construisez l'application
RUN ./gradlew build
RUN ./gradlew shadowJar

FROM alpine:latest

WORKDIR /opt/omega-bot/
COPY --from=build-stage /builder/build/libs/Omega-Bot-1.0-SNAPSHOT-all.jar /opt/omega-bot/omega-bot.jar
COPY --from=build-stage /builder/data /opt/omega-bot/data

RUN apk add --no-cache openjdk21-jre

# Démarrez l'application
CMD ["java", "-jar", "omega-bot.jar"]
