package com.vend.http

import cats.data.{Kleisli, OptionT}
import cats.effect.{IO, Resource}
import cats.implicits.toSemigroupKOps
import com.comcast.ip4s.{Host, Port}
import com.typesafe.scalalogging.StrictLogging
import io.prometheus.client.CollectorRegistry
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.metrics.prometheus.Prometheus
import org.http4s.server.middleware.{Logger, Metrics}
import org.http4s.server.{Router, Server}
import org.http4s.{HttpApp, HttpRoutes, Request, Response}

/** Interprets the endpoint descriptions (defined using tapir) as http4s routes, adding CORS, metrics, api docs and correlation id support.
 *
 * The following endpoints are exposed:
 *   - `/api/v1` - the main API
 *   - `/api/v1/docs` - swagger UI for the main API
 *   - `/admin` - admin API
 *   - `/` - serving frontend resources
 */
class HttpApi(endpoints: HttpRoutes[IO], adminEndpoints: HttpRoutes[IO], collectorRegistry: CollectorRegistry, config: HttpConfig)
  extends StrictLogging {
  private val apiContextPath = "/api"
  private val adminContextPath = "/admin"
  private val respondWithNotFound: HttpRoutes[IO] = Kleisli(_ => OptionT.pure(Response.notFound))

  //  private lazy val corsConfig: CORSConfig = CORSConfig.default

  lazy val resource: Resource[IO, Server] = {
    val prometheusHttp4sMetrics = Prometheus.metricsOps[IO](collectorRegistry)
    prometheusHttp4sMetrics
      .map(m => Metrics[IO](m)(loggingMiddleware(endpoints)))
      .flatMap { monitoredRoutes =>
        val app: HttpApp[IO] = Router(
          apiContextPath -> (monitoredRoutes <+> respondWithNotFound),
          adminContextPath -> adminEndpoints
        ).orNotFound

        val finalHttpApp = Logger.httpApp(true, true)(app)

        EmberServerBuilder.default[IO]
          .withHost(Host.fromString(config.host).get)
          .withPort(Port.fromInt(config.port).get)
          .withHttpApp(finalHttpApp)
          .build
      }
  }

  private def loggingMiddleware(service: HttpRoutes[IO]): HttpRoutes[IO] = Kleisli { req: Request[IO] =>
    OptionT(for {
      _ <- IO(logger.debug(s"Starting request to: ${req.method} ${req.uri.path}"))
      r <- service(req).value
    } yield r)
  }

}
