package com.vend.config

import com.vend.http.HttpConfig
import com.vend.infrastructure.DBConfig

import scala.concurrent.duration.FiniteDuration

final case class Config(
  shutdownSleep: FiniteDuration,
  api: HttpConfig,
  db: DBConfig,
  httpClient: Config.HttpClient,
)

object Config {
  final case class HttpClient(connectionTimeout: FiniteDuration, readTimeout: FiniteDuration)
}
