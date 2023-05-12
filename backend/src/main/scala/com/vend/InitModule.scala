package com.vend

import cats.effect.{IO, Resource}
import com.vend.config.ConfigModule
import com.vend.infrastructure.DB
import org.http4s.client.Client
import org.http4s.ember.client.EmberClientBuilder

/** Initialised resources needed by the application to start.
  */
trait InitModule extends ConfigModule {
  lazy val client: Resource[IO, Client[IO]] =
    EmberClientBuilder.default[IO].withTimeout(config.httpClient.connectionTimeout).build

  lazy val db: DB = new DB(config.db)
}
