# Sample App #

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
* [cats-effect 2](https://typelevel.org/cats-effect/docs/2.x/getting-started) - Functional library for handling effectual operations
* [doobie](https://tpolecat.github.io/doobie/) - Database queries
* [Flyway](https://flywaydb.org/documentation/) - For DB migrations


## Configuration
You can configure the application by setting environment variables. You can check the list of available options in `src/main/resources/application.conf`.


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
1. Run the database with `docker-compose up pg_vend_db`.
2. Create run configuration for `Main` object with environment variables: `SQL_PORT=5432;SQL_PASSWORD=kermit`.

### Fat jar
1. Run the database with `docker-compose up pg_vend_db`.
2. Execute `sh start.sh`.

### Tests ###
* [scalatest](https://github.com/scalatest/scalatest) - is a testing toolkit for Scala.
* [ScalaMock](https://github.com/paulbutcher/ScalaMock) - is a mocking toolkit for Scala.
* [scoverage](http://scoverage.org) - is a code coverage tool for Scala.

To run the unit tests `sbt clean test`

To run the tests and generate a coverage report: `sbt clean coverage test coverageReport`. The resulting report will be output to the `/target/scala-2.13/scoverage-report` directory.

## API endpoints ##

http://localhost:3000/api/vend/purchase
http://localhost:3000/api/vend/inventory

http://localhost:3000/api/audit/inventory/<productId>
http://localhost:3000/api/audit/ledger
http://localhost:3000/api/audit/ledger/<ledgerId>


## Deployment guide ##

After the database has started,

1.insert a record into the vend.ledger_token table:
```
INSERT INTO auth.ledger_token(ledger_id, session_token, account_token, expire_date_time)
VALUES ('123', '', '', '2021-12-17 08:04:00.015733');
```

2.insert a record into the token_refresh_job table:
```
INSERT INTO auth.token_refresh_job(ledger_token_id, status, updated, expire_date_time)
VALUES (1, 'valid', '2021-12-17 08:04:00.015733', '2021-12-17 08:04:00.015733');
```


In this ERD, there are four entities:

PRODUCT: Represents a product in the vending machine, with properties for the product name, price, and quantity.
TRANSACTION: Represents a transaction in the vending machine, with properties for the transaction date and the amount paid by the customer.
LEDGER: Represents a product purchased in a transaction, with properties for the quantity purchased and a foreign key to the transaction and product entities.
PAYMENT: Represents a payment made by the customer, with properties for the transaction ID and the payment amount.

+----------------+       +----------------+           +----------------+       +----------------+
|    PRODUCT     |       |  TRANSACTION   |           | LEDGER         |       | PAYMENT        |
+----------------+       +----------------+           +----------------+       +----------------+
| id: Int        | 1..*  | id: Int        |           | txn_id: Int    |       | id: Int        |
| name: String   |<>-----| total: Double  | 1..*      | product_id: Int|       | txn_id: Int    |
| price: Double  |       | date: DateTime |<>---------| quantity: Int  |       | amount: Double |
| quantity: Int  |       +----------------+           +----------------+       +----------------+
+----------------+

The relationships between the entities are as follows:

PRODUCT and LEDGER have a one-to-many relationship, as one product can be purchased in many transactions, but each transaction only contains one product.
TRANSACTION and LEDGER have a one-to-many relationship, as one transaction can contain many products, but each product is only purchased in one transaction.
TRANSACTION and PAYMENT have a one-to-one relationship, as each transaction corresponds to one payment.

DESIGN:
- the UI will constrain the quantity of products available for purchase by requesting inventory from the backend
- 

ASSUMPTIONS:
- the vending machine has a payment processor that handles the payment process
- this payment processor calculates and verifies the payment amount