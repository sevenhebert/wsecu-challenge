# Backend Vending Machine App #

## Prerequisites

* Java 11
* Scala 2.13
* Sbt 1.5
* Docker

## Developer guide ##

During development, you can start (and restart) the application via the `sbt reStart`
sbt task provided by the sbt-revolver plugin.

## Built with

* [cats](https://typelevel.org/cats/) - Library for functional programming
* [cats-effect 2](https://typelevel.org/cats-effect/docs/2.x/getting-started) - Functional library for handling
  effectual operations
* [doobie](https://tpolecat.github.io/doobie/) - Database queries
* [Flyway](https://flywaydb.org/documentation/) - For DB migrations

## Configuration

You can configure the application by setting environment variables. You can check the list of available options
in `src/main/resources/application.conf`.

## Running locally

To run the service locally, the service needs to be pointed at some postgres database instance.
See the `application.conf` `db` section.

`sbt run`

or inside a Scala repl/SBT using [sbt-revolver](https://github.com/spray/sbt-revolver):

* `reStart` starts the application in a forked JVM
* `~reStart` starts and recompiles the application when changes are detected
* `reStop` stops the application
* `reStatus` shows an informational message about the current running state of the application

### Docker-compose

1. Build and run docker image for the application with `docker-compose up --build`.

### IntelliJ

1. Run the database with `docker-compose up vend`.
2. Create run configuration for `Main` object with environment variables: `SQL_PORT=5432;SQL_PASSWORD=kermit`.

### Fat jar

1. Run the database with `docker-compose up vend`.
2. Execute `sh start.sh`.

### Tests ###

* [scalatest](https://github.com/scalatest/scalatest) - is a testing toolkit for Scala.
* [ScalaMock](https://github.com/paulbutcher/ScalaMock) - is a mocking toolkit for Scala.
* [scoverage](http://scoverage.org) - is a code coverage tool for Scala.

To run the unit tests `sbt clean test`

To run the tests and generate a coverage report: `sbt clean coverage test coverageReport`. The resulting report will be
output to the `/target/scala-2.13/scoverage-report` directory.

## API endpoints ##

# Frontend endpoints #

http://localhost:8080/api/vend/purchase
http://localhost:8080/api/vend/inventory

# Admin endpoints #

http://localhost:8080/api/audit/inventory/<productId>
http://localhost:8080/api/audit/ledger
http://localhost:8080/api/audit/ledger/<ledgerId>
