FROM openjdk:11

LABEL author="Roberto Rocha"
LABEL email="rrocha.roberto@gmail.com"
LABEL data="22/09/2020"

ARG DB_PASSWORD
ARG WHEATHER_APPID

ENV DB_PASS=$DB_PASSWORD
ENV APPID=$WHEATHER_APPID

WORKDIR /usr/local/bin

#Transfer the war file to imagem
COPY ./target/field-backend-0.0.1-SNAPSHOT.jar field-backend.jar

#execute java application inside the container

ENTRYPOINT [ "sh", "-c", "java -Dspring.profiles.active=docker -Dspring.datasource.password=$DB_PASS -Dweather.appid=$APPID -jar field-backend.jar" ]

