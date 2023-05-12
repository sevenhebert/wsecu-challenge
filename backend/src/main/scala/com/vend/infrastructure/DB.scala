package com.vend.infrastructure

import cats.effect._
import cats.implicits._

import com.typesafe.scalalogging.StrictLogging
import doobie._
import doobie.hikari._
import doobie.implicits._
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.Location

import scala.concurrent.duration._

/** Configures the database, setting up the connection pool and performing migrations.
  */
class DB(config: DBConfig) extends StrictLogging {
  val transactorResource: Resource[IO, Transactor[IO]] = {

    /*
     * connectEC this is a thread pool for awaiting connections to the database. There might be an arbitrary
     * number of clients waiting for a connection, so this should be bounded.
     *
     * See also: https://tpolecat.github.io/doobie/docs/14-Managing-Connections.html#about-threading
     */
    for {
      connectEC <- ExecutionContexts.fixedThreadPool[IO](config.connectThreadPoolSize)
      xa <- HikariTransactor.newHikariTransactor[IO](
        config.driver,
        config.url,
        config.username,
        config.password.value,
        connectEC
      )
      _ <- Resource.eval(connectAndMigrate(xa))
    } yield xa
  }

  @SuppressWarnings(Array("org.wartremover.warts.Recursion"))
  private def connectAndMigrate(xa: Transactor[IO]): IO[Unit] = {
    (migrate() >> testConnection(xa) >> IO(logger.info("Database migration & connection test complete"))).recoverWith {
      case e: Exception =>
        logger.warn("Database not available, waiting 5 seconds to retry...", e)
        IO.sleep(5.seconds) >> connectAndMigrate(xa)
    }
  }

  private val schemas: Seq[String] = Seq("vend")

  private val flyway = {
    Flyway
      .configure()
      .schemas(schemas: _*)
      .dataSource(config.url, config.username, config.password.value)
      .outOfOrder(true)
      .locations(
        config.migrationLocations
          .map(new Location(_)): _*
      )
      .load()
  }

  private def migrate(): IO[Unit] = {
    config.migrationOptions match {
      case "MigrateOnStart" => IO(flyway.migrate()).void
      case "RepairOnStart" => IO(flyway.repair()).void
      case _ => IO.unit
    }
  }

  private def testConnection(xa: Transactor[IO]): IO[Unit] =
    IO {
      sql"select 1".query[Int].unique.transact(xa)
    }.void

}
