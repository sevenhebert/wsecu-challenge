package com.vend.infrastructure

import com.vend.config.Sensitive

final case class DBConfig(
  username: String,
  password: Sensitive,
  name: String,
  host: String,
  port: Int,
  migrationOptions: String,
  driver: String,
  connectThreadPoolSize: Int,
  migrationLocations: List[String]
) {
  def url: String = s"jdbc:postgresql://$host:$port/$name"
}
