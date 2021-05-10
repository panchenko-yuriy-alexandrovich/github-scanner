# github-scanner
scan github for repositories with speciifc names.

This API is scanning GihHub with GitHub API, calculates unique repo names
and is returning current search and previous one with calculated difference.

- Server API is using Jooby
- DB is Postgres
- Liquibase migrates the DB
- Server unit tests are with Junit5/Mockito
- H2 is for DB integration tests
- system-stubs-jupiter for ENV tests

This app is running inside [Heroku](https://github-scanner.herokuapp.com/api/health) 

Some test collections you could find in [postman-collection](https://github.com/panchenko-yuriy-alexandrovich/github-scanner/blob/main/tests.postman_collection.json")

This repo was inspired by Jpoint 2021 [Andrei Solntsev](https://github.com/asolntsev) and [Anton Keks](https://github.com/angryziber) [presentation](https://github.com/angryziber/jpoint-pairing-2021)
I have tried to follow their steps and create a working tested product.
It was an interesting journey for Mays vacation.

#Noteworthy features:
- Custom dependency injection
- Efficient [Docker](https://github.com/panchenko-yuriy-alexandrovich/github-scanner/blob/main/Dockerfile) usage (cached layers in order of less frequent changes), maven downloads dependencies once

#Running in Docker

docker-compose up --build

or to just start the DB: docker-compose up -d db

This will bind to 127.0.0.1:6432 by default
#Development
After clone:

To run - ./mvnw clean compile exec:java

To test - ./mvnw test

#Deployment
- [Jenkinsfile](https://github.com/panchenko-yuriy-alexandrovich/github-scanner/blob/main/Jenkinsfile) is used to deploy to Heroku
- Env-specific configuration is provided using env vars (docker-compose.yml files or Heroku)
- All env vars are optional, so that everything would run out of the box in development