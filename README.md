# fields-data
This project offers a simple rest API to manage fields data and retrieve weather history of a field.

# System Requirements

## 1) Field managing

A Rest API access through `/fields` endpoint.

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

Please refer to the following diagrams for specific design view:

- [architectural diagram](docs/Architecture.png) for a high level visual representation.

- [JSON class diagram](docs/JsonClassModel.png) for a class structure of JSON field API.

- [Rest class diagram](docs/RestClassModel.png) for a structure regards Rest implementation.

- [History weather process](docs/ReteiveWeatherHistoryProcess.png) for the steps for retrieving the data.

- [History weather class diagram](docs/WeatherHistoryModel.png) for class structure of JSON history weather API.


## Java Project

- Provides the Rest API's regarding the both requirements defined before in this document.

- The Rest API's use Spring framework to provide resources for easy implementation.

- Uses JPA/Hibernate to easy interact (persist and retrieve data) with relational database.

- Uses SpringBoot to easily publish the project in a web container.


## Database

- Contains the tables to persist the data used in the application.

Please refer to this [physical model](docs/dbPhysicalModel.pdf) for a database relational specification.

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

For accessing all resources of the implemented API, use a complete [TODO: Postman collection](docs/Fields.postman_collection.json).


# Notes and assumptions

1) Create issues on Github to manage the tasks of the project.

2) Create a `docs` directory to keep the specific documentation files.


3) Regards to database model (created using SQLPowerArchitect tool):

	3.1) It's not necessary define one table for each JSON message level (see [physical model](docs/dbPhysicalModel.pdf).

	3.2) For simplifying the design, the properties field is not mapped to the database yet (it is recursively).

	3.3) The "geoJson" and its children nodes (from JSON) will be mixed and stored in only one table (Coordinates). This table supports only one "coordinates" object (from JSON). The type attributes will be mapped only inside the source code (as constants). Coordinates of a hole is not supported (different of rfc7946).

	3.4) The tables Field and Boundary was created in order to separate their specific data and this approach can support future multiple boundaries for a field.

	3.5) The table Coordinate manages only coordinates data.

	3.6) Precision of the following fields: Field id, Field name, Boundary id, Country code, latitude and longitude: (TODO: needs review based on Daniel's email)

[//]: # (latitude and longitude will support 3 integer digits and 20 decimal digits)


4) Regards to JSON validation:

	4.1) The JSON fields: "id",  "name", "countryCode", "bounderies", "geoJson", "geometry", "type" (geoJson and geometry), "coordinates" are mandatory.

	4.2) "properties" is optional (and will not handled now).

	4.3) Only the node "coordinates" won't be validated.

	4.4) Only geometry type "Polygon" (from rfc7946) will be accepted and persisted.

	4.5) Field "bounderies" has different spelling.

	4.6) The first implementation will support only exterior ring for coordinates.


5) Regards to Java project (CRUD Field API):

	5.1) The base project was created using [Spring Initializr](https://start.spring.io) (Spring Boot v2.3.4), with the following dependencies: Spring Web, DevTools, Data JPA and PostgreSQL Driver.

	5.2) The database script was generated using SQLPowerArchitect model. The coordinate_id field was changed manually to SERIAL (modeling tool issue).

	5.3) For running in `dev` environment, setup the PostgreSQL database using the files to [configure the database](database/configureDatabase.sql) and [create the tables](database/script.sql).

	5.4) Used BigDecimal for latitude and longitude values (double didn't support).

	5.5) Before perform update a field, it cleans up the coordinates of current boundary, because the amount o boundaries may change in the new data.

	5.6) It was created two specific (unchecked) exceptions for handling and sending good format responses in case of some inconsistencies: FieldException and FieldNotFoundException. Unchecked to avoid declaring everywere. Only method handleException(...) in FieldController class handles them properly.

	5.7) Any exception (bellow Exception class) will result in a BAD REQUEST to client. 

	5.8) Implemented GeneralExceptionHandler class to handle any general exceptions.

	5.9) The service class FieldService serves the CRUD operations to the Rest controller.

	5.10) The utility class FieldDataConverter converts JSON to entities objects and vice versa.]

	5.11) The utility class DateUtil converts String to Date objects and vice versa.


6) Regards to Java project (Historical Weather API):

	6.1) GeoData and Geometry classes will be reused in Polygon JSON structure.

	6.2) Only "main" data will be mapped to receive data from OpenWeather Agro Monitoring API.

	6.3) appid for polygon API: fedc87646176973e8fa85df97a04f0fc

	6.4) Using the fieldId as name of polygon.





