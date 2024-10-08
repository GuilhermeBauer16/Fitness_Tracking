FROM openjdk:21-jdk-slim

ARG DB_NAME
ARG DB_USERNAME
ARG DB_PASSWORD
ARG DB_PORT
ARG SECRET_KEY


ENV DB_NAME=${DB_NAME} \
    DB_USERNAME=${DB_USERNAME} \
    DB_PASSWORD=${DB_PASSWORD} \
    DB_PORT=${DB_PORT} \
    SECRET_KEY=${SECRET_KEY}


WORKDIR /app

COPY target/*.jar /app/FitnessTracking.jar

ENTRYPOINT ["java", "-jar", "FitnessTracking.jar"]
