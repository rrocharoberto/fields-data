# fields-data
This project contains a simple rest API to manage fields data and retrieve weather history of the fields.

# Rest API Requirements

This project has two use cases:

## 1) Field management

A Rest API access through `/fields` endpoint that should offer CRUD operations for field data.

The JSON structure is defined in a private document.

### Restrictions: 

- no partial updates

- no boundary validation

- no authentication

## 2) Weather history

Rest API that fetches weather history of a field boundary. The data should be retrieved using API from [OpenWeather Agro Monitoring](https://agromonitoring.com/api).

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

- [Architectural diagram](docs/Architecture.png) for a high level visual representation.

- [JSON class diagram](docs/JsonClassModel.png) for a class structure of JSON field API.

- [Rest class diagram](docs/RestClassModel.png) for a structure regards Rest implementation.

- [History weather process](docs/ReteiveWeatherHistoryProcess.png) for the steps for retrieving the data.

- [History weather class diagram](docs/WeatherHistoryModel.png) for class structure of JSON history weather API.


## Java Project

- Provides the Rest API's regarding the both requirements defined before in this document.

- Uses Rest API's use Spring framework to provide resources for easy implementation.

- Uses JPA/Hibernate to easy interact (persist and retrieve data) with relational database.

- Uses SpringBoot to easily publish the project in a web container.

- Has a few tests.


## Database

- Contains the tables to persist the data used in the application.

Please refer to this [physical model](docs/dbPhysicalModel.pdf) for a database relational specification.

## OpenWeather Agro Monitoring

- Provides the information of weather history for a specified field, using it's polygon.



# Development Environment

This project uses the following tools:
- Java - OpenJDK 11
- Maven 3.5
- Docker 19.03
- Eclipse JEE 2020-06

The docker containers uses the following tools:

- OpenJDK 11
- PostgreSQL 12




# Building process

The building process prepares the project to run it in a Docker environment. 

## Build backend project

In order to build the Spring Boot project, execute the following command inside the `field-backend` directory:

`mvn clean install`

This command will also run the unit and integration tests.


## Create the backend Docker image `backend-fields`

In order to build the Docker image of Spring Boot application, execute the following command inside the `field-backend` directory:

`docker build --build-arg DB_PASSWORD=$DB_PASSWORD --build-arg WHEATHER_APPID=$WHEATHER_APPID -t backend-fields .`

Change `$DB_PASSWORD` to your own database password and and `$WHEATHER_APPID` to your appid from OpenWeather API.


## Create the database image `db-fields`

In order to build the Docker image of database, execute the following command inside the `database` directory:

`docker build --build-arg DB_PASSWORD=$DB_PASSWORD -t db-fields .`

Change `$DB_PASSWORD` to your own database password (same as `backend-fields` container).



# Running process

This section describes how to run the application using the Docker container environment.


### Configure the Docker network

`docker network create fields-net`


### Run the database container

`docker container run -d --rm -p 5433:5432 --network fields-net -v $DATABASE_DATA_DIR:/var/lib/postgresql/data --name fields-dbserver db-fields`

Change `$DATABASE_DATA_DIR` to your own database directory.

PS: external port was changed to avoid conflict with the host default PostgreSQL instance.


### Run the backend application container

`docker container run -d --rm -p 8080:8080 --network fields-net --name fields-backend backend-fields`


## Run in development environment

Import the `field-backend` project into Eclipse and perform "Update" in "Maven" menu.

Before running the application, setup the PostgreSQL database using the script files [configure the database](database/configureDatabase.sql) and [create the tables](database/script.sql). Don't forget to change the password properly (DB_PASSWORD) in `configureDatabase.sql` file.

Also change the database properties in file [application.properties](field-backend/src/main/resources/application.properties) properly.

Run the `main` method of the class `com.roberto.field.FieldBackendApplication`


# Accessing the services

Before access the services, you should check the [Field API documentation](http://localhost:8080/field-backend/swagger-ui.html) to see more details of the API.


The following GET requests can be made using a web browser or a `curl` command:

- `http://localhost:8080/fields`

- `http://localhost:8080/fields/{fieldId}`

- `http://localhost:8080/fields/{fieldId}/weather`

PS: change the `localhost` address properly if you are accessing from another machine.

Use the `fieldId` of your choice.

For accessing all resources of the implemented APIs, use this complete [Postman collection](docs/Fields.postman_collection.json).



# Reports

For a basic test report, execute the command bellow (inside `field-backend` directory) and open the file `field-backend/target/site/surefire-report.html`.

`mvn site`


# Notes and assumptions

1) Create issues on Github to manage the tasks of the project.

2) Create a `docs` directory to keep the specific documentation files.


3) Regards to database model (created using SQLPowerArchitect tool):

	3.1) It's not necessary define one table for each JSON message level (see [physical model](docs/dbPhysicalModel.pdf)).

	3.2) For simplifying the design, the `properties` field is not mapped to the database yet (it is recursively).

	3.3) Also, the field `type` of `geoJson` is not validated. Its default value is `"Feature"`. 

	3.4) The "geoJson" and its children nodes (from JSON) will be mixed and stored in only one table (`Coordinate`). This table supports only one "coordinates" object (from JSON). The type attributes will be mapped only inside the source code (as constants). Coordinates of a hole is not supported (different of [rfc7946](https://tools.ietf.org/html/rfc7946)).

	3.5) The tables `Field` and `Boundary` were created in order to separate their specific data and this approach can support future multiple boundaries for a field.

	3.6) Precision of the following fields: Field id, Field name, Boundary id, Country code, latitude and longitude: answered by Daniel. Latitude and longitude will support 3 integer digits and 20 decimal digits.

[//]: # (some comment)



4) Regards to JSON validation:

	4.1) The JSON fields: "id",  "name", "countryCode", "bounderies", "geoJson", "geometry" (not "type") (geoJson and geometry), "coordinates" are mandatory.

	4.2) "properties" is optional (and will not be handled now).

	4.3) Only the node "coordinates" won't be validated.

	4.4) Only geometry type "Polygon" (from rfc7946) will be accepted and persisted.

	4.5) Field "bounderies" has different spelling.

	4.6) The first implementation will support only exterior ring for coordinates.



5) Regards to Java project (CRUD Field API):

	5.1) The base project was created using [Spring Initializr](https://start.spring.io) (Spring Boot v2.3.4), with the following dependencies: Spring Web, DevTools, Data JPA and PostgreSQL Driver.

	5.2) The database script was generated using SQLPowerArchitect model. The `coordinate_id` field was changed manually to SERIAL (modeling tool issue).

	5.3) Used BigDecimal for latitude and longitude values (double didn't support).

	5.4) Before perform update of a field, it cleans up the coordinates of current boundary, because the amount o boundaries may change in the new data.

	5.5) It was created two specific (unchecked) exceptions for handling and sending good format responses in case of some inconsistencies: `FieldException` and `FieldNotFoundException`. The choice of unchecked ones was to avoid declaring it everywhere. Only method `handleException(...)` in `FieldController` class handles them properly.

	5.6) Any exception (bellow `Exception` class hierarchy) will result in a BAD REQUEST to client. It was implemented `GeneralExceptionHandler` class to handle any general exceptions.

	5.7) The service classes `FieldService` and `WeatherService` serve the CRUD operations and weather resources to the Rest controller.

	5.8) The utility class `FieldDataConverter` converts JSON to entities objects and vice versa.

	5.9) The utility class `DateUtil` converts String to Date objects and vice versa.

	5.10) In `FieldEntity`, attribute `created` is set only when field is persisted to database and `updated` is set every time update (PUT) is performed.

	5.11) When updating a field, it keeps the same `polygonId` (of a polygon already created in the past).


6) Regards to Java project (Historical Weather API):

	6.1) `GeoData` and `Geometry` classes will be reused in `Polygon` API JSON structure.

	6.2) Only `main` data will be mapped to receive data from OpenWeather Agro Monitoring API.

	6.3) `appid` for polygon API not is stored in application.properties file anymore. It is a command line parameter now.

	6.4) Using the `fieldId` as name of polygon (in `PolygonDataRequest`).

	6.5) Added extra dependency `spring-boot-starter-webflux` in order to implement Rest client for polygon and weather history APIs.

	6.6) Official API of weather history gives unauthorized error. Using sample API (as answered by Daniel) for tests.

	6.7) Note: the data in response from weather history (using sample API) are only samples (they are not compatible with the dates used to query it). [Example used](https://samples.openweathermap.org/agro/1.0/weather/history?appid={$appid}&polyid=5f69118a714b526842e124ff&start=1600710398&end=1600191998) (change `appid` properly).

	6.8) Change property `historical.weather.rest.url` in file `/field-backend/src/main/resources/application.properties` to configure the weather history API properly.

	6.9) Catching exceptions when retrieving data from OpenWeather API and handling it as `FieldAPIException` (for bad gateway HTTP response).

	6.10) New field `polygon_id` in table `Boundary` to save id of the polygon created in OpenWeather API. Avoid creating new polygon when it is already there. The OpenWeather API keeps the polygons. When there is a request, the code checks if it is necessary create a new polygon.

	6.11) Special note of [polygon API](from https://agromonitoring.com/api/polygons): "When creating a polygon, the first and last positions are equivalent, and they MUST contain identical values" (example of incorrect API call). The code checks if the first has the same value as the last element, if not, add the first at the end of the list.

	6.12) The `updated` field of `Boundary` table is not changed when Weather API creates a polygon in Agro Monitoring API. It is updated only when the Rest client updates the field.


7) Regards to Docker environment

	7.1) Database port is different from default to avoid port in use conflict with local development database.

	7.2) All passwords and appid (OpenWeather key) must be configured in command line parameters.

	7.3) Both containers use the same `fields-net` network.



8) Regards to Tests

	8.1) Integration test (only create and retrieve fields) are defined in `FieldBackendApplicationTests` class.

	8.2) Only unit test of weather history service is defined in `WeatherServiceTests` class. 

	8.3) Unit test uses Mockito to mock `FieldDAO` and `WeatherServiceDataRetriever`. Methods mocked `findById(...)`, `doCreatePolygon(...)` and `doRetrieveHistoricalWeather(...)`.

	8.4) Integration test uses H2 in memory database configured in `field-backend/src/test/resources/application.properties` file.



