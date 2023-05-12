package com.vend.config

import com.typesafe.scalalogging.StrictLogging

/** Reads and gives access to the configuration object.
  */
trait ConfigModule extends StrictLogging {

  def config: Config

  def logConfig(): Unit = {
    val baseInfo =
      s"""
         |Bootzooka configuration:
         |-----------------------
         |API:                ${config.api}
         |DB:                 ${config.db}
         |HttpClient:         ${config.httpClient}
         |""".stripMargin

    logger.debug(baseInfo)
  }
}
