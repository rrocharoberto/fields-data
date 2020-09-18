# fields-data
This project offers a simple rest API to manage fields data and retrieve weather history of a field.

# System Requirements

## 1) Field managing

A Restful API access through `/fields` endpoint.

The JSON structure is defined in a private document.

### Restrictions: 

- no partial updates

- no boundary validation

- no authentication

## 2) Weather history

Rest API that fetches weather history of a field boundary. The data should be retrieved using API from OpenWeather Agro Monitoring.

The API endpoint is available through GET request endpoint: `/fields/{fieldId}/weather` .

The API should retrieve the following data: temperature, temperature min, temperature max and humidity for the last 7 days.

The JSON structure is defined in a private document.

### Restrictions: 

- retrieve data from [polygons](https://agromonitoring.com/api/polygons) and [history weather](https://agromonitoring.com/api/history-weather) API's.

- no performance required

- no authentication

# Solution Architecture

The solution has three specific nodes:

- the Java (11) project using Spring and SpringBoot

- the relational database

- one external component (OpenWeather Agro Monitoring)

Please refer to this [TODO: architectural diagram](docs/architecture.png) for a visual representation.

## Java Project

- Provides the Rest API's regarding the both requirements defined before in this document.

- The Rest API's use Spring framework to provide resources for easy implementation.

- Uses JPA/Hibernate to easy interact (persist and retrieve data) with relational database.

- Uses SpringBoot to easily publish the project in a web container.

## Database

- Contains the tables to persist the data used in the application.

Please refer to this [TODO: physical model](docs/dbPhysicalModel.png) for a database relational specification.

## OpenWeather Agro Monitoring

- Provides the information of weather history for a specified field, using it's polygon.

# Development Environment

This project uses this tools:
- Java - OpenJDK 11
- Maven 3.5
- Docker 19.03
- Eclipse JEE 2020-06

The docker containers uses the following tools:

- Apache Tomcat (with Java - OpenJDK 11
)
- PostgreSQL 12

# Building process

## Build backend project

TODO: describe it

## Create the backend docker image `backend-fields`

TODO: describe it


## Create the database image `db-fields`

TODO: describe it


# Running process

TODO: describe it


### Create the docker network

TODO: describe it


### Run the database container

TODO: describe it


### Run the backend application container

TODO: describe it


# Accessing the services

The following GET requests can be made using a web browser or a `curl` command:

- `http://localhost:8080/fields`

- `http://localhost:8080/fields/{fieldId}`

- `http://localhost:8080/fields/{fieldId}/weather`

PS: change the `localhost` address properly if you are accessing from another machine.

For accessing all resources of the implemented API, use a complete [TODO: Postman collection](docs/fields.postman_collection.json).


# Notes and assumptions

1) Create issues on Github to manage the tasks of the project.

2) Create a `docs` directory to keep the specific documentation files.


