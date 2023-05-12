package com.vend.metrics

import cats.effect.IO
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.common.TextFormat
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

import java.io.StringWriter

/** Defines an endpoint which exposes the current state of the metrics, which can be later read by a Prometheus server.
  */
class MetricsApi(registry: CollectorRegistry) {

  val routes: HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._

    HttpRoutes.of[IO] {
      case GET -> Root / "metrics" =>
        val writer = new StringWriter
        TextFormat.write004(writer, registry.metricFamilySamples)
        Ok(writer.toString)
    }
  }

}
