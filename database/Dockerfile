FROM postgres:12.3-alpine

LABEL author="Roberto"
LABEL email="rrocha.roberto@gmail.com"
LABEL data="22/09/2020"

# comes from command line argument
ARG DB_PASSWORD

ENV POSTGRES_USER=field

ENV POSTGRES_PASSWORD=$DB_PASSWORD
ENV POSTGRES_DB=field

#Transfer the database script file to imagem
COPY ./script.sql /docker-entrypoint-initdb.d/script.sql

EXPOSE 5432

#execute postgres inside the container
CMD ["postgres"]

