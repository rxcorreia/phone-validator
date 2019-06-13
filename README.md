# OLX Challenge - Phone Number Validator

South African mobile numbers validation.

## Project overview

### Motivation

Backend Engineer Assessment Exercise to perform validation of South African mobile numbers, provided through a CSV file.



### Documentation

Project API Swagger file can be found [here](apis/swagger.yaml).

## Quick Start

### Running Locally

#### Database

This service requires access to a database. For that purpose, a `docker-compose.yaml` was included to allow for any
dependency to be started locally, mainly a Postgres DB available at port 5432. To get them up-and running just run the `up` command on the root of the repository:

```
docker-compose up
```

#### Build

The build task will build the `jar` and run unit tests. To do so,
simply run:

```
./gradlew clean build
```

#### Run

To run the application locally, `default` Spring profile can be used. Application properties sets the data source to
the local database started with `docker-compose`, running at port 5432.

```
./gradlew clean bootRun
```

On Windows:

```
SET SPRING_PROFILES_ACTIVE=local
gradlew clean bootRun
```
### Usage
For usage details, check API documentation.


### Design Decisions
Mainly due to time constrains which affected development of solution, mind the following implementation rationales and shortcomings/areas of improvement:
* Support libraries
    * Spring Boot + Web/Data/Test starters to help accelerate development.
    * Flyway DB migration tool to help define DDL/DB schemas.
* Number validation:
    * SA mobile number format validation was based on work by Warwick Chapman ([URL](http://wa.rwick.com/2016/06/09/validating-south-african-cell-numbers-with-a-regular-expression-regex/)).
    * Fixing of incorrect phone numbers was only based on quick analysis of provided dataset and assuming fixable values would be the ones with valid format plus a giving suffix (e.g. '27735405794_DELETED_1488987214').
* API implementation
    * No security implemented to restrict access to endpoints.
    * File validation issues (empty content, invalid mime type...) will abort upload process and return a BAD_REQUEST/400 HTTP error code.
    * More consistent error handling, including more fine grained definition of different error situations, is needed.
    * No listing/search functionality to get processed file results 
    * Logging is missing
* Testing 
    * Only a small number were tests were written, hence coverage is low. Main tests cover JPA repository functioning and phone number format validation (the core feature of the project).