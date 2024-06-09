FROM amazoncorretto:21
LABEL authors="leroideskiwis"

RUN mkdir /opt/omega-bot/
WORKDIR /opt/omega-bot/
COPY . .

RUN ./gradlew build
EXPOSE 8080

ENV BOT_TOKEN=YOUR_TOKEN
ENV LOG_CHANNEL_ID=YOUR_CHANNEL_ID

CMD ["/opt/omega-bot/gradlew", "run"]