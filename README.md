# github-scanner
scan github for repositories with speciifc names
the API is scanning GihHub with GitHub API calculate unique repo names
and returning current search and previous one with calculated difference.

- Server API is using Jooby
- DB is Postgres
- Liquibase migrates the DB
- Server unit tests are with Junit5/Mockito
- H2 is for DB integration tests
- system-stubs-jupiter for ENV tests

This repo was inspired by Jpoint2021 Andrew Solntsev and Anton Keks presentation.
I have tried to follow their steps and create a working tested product.

#Noteworthy features:
- Custom dependency injection
- Efficient Docker usage (cached layers in order of less frequent changes), maven downloads dependencies once

#Running in Docker

docker-compose up --build

or to just start the DB: docker-compose up -d db

This will bind to 127.0.0.1:6432 by default
#Development
After clone:

To run - ./mvnw clean compile exec:java

To test - ./mvnw test

#Deployment
- Jenkinsfile is used to deploy to Heroku
- Env-specific configuration is provided using env vars (docker-compose.yml files or Heroku)
- All env vars are optional, so that everything would run out of the box in development