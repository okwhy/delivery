
FROM openjdk:17-jdk-slim


WORKDIR /app


COPY build/libs/delivery-0.0.1-SNAPSHOT.jar  app.jar


ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]


EXPOSE 8080
