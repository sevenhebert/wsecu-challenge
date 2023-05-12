package com.vend.http

import cats.data.{Kleisli, OptionT}
import cats.effect.{IO, Resource}
import cats.implicits.toSemigroupKOps
import com.comcast.ip4s.{Host, Port}
import com.typesafe.scalalogging.StrictLogging
import io.prometheus.client.CollectorRegistry
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.headers.Origin
import org.http4s.metrics.prometheus.Prometheus
import org.http4s.server.middleware.{CORS, Logger, Metrics}
import org.http4s.server.{Router, Server}
import org.http4s.{HttpApp, HttpRoutes, Request, Response, Uri}

/** Interprets the endpoint descriptions (defined using tapir) as http4s routes, adding CORS, metrics, api docs and correlation id support.
 *
 * The following endpoints are exposed:
 *   - `/api/vend` - the main API
 *   - `/admin` - admin API
 */
class HttpApi(endpoints: HttpRoutes[IO], adminEndpoints: HttpRoutes[IO], collectorRegistry: CollectorRegistry, config: HttpConfig)
  extends StrictLogging {
  private val apiContextPath = "/api"
  private val adminContextPath = "/admin"
  private val respondWithNotFound: HttpRoutes[IO] = Kleisli(_ => OptionT.pure(Response.notFound))

  lazy val resource: Resource[IO, Server] = {
    val prometheusHttp4sMetrics = Prometheus.metricsOps[IO](collectorRegistry)
    prometheusHttp4sMetrics
      .map(m => Metrics[IO](m)(loggingMiddleware(endpoints)))
      .flatMap { monitoredRoutes =>
        val app: HttpApp[IO] = Router(
          apiContextPath -> (monitoredRoutes <+> respondWithNotFound),
          adminContextPath -> adminEndpoints
        ).orNotFound

        val finalHttpApp = Logger.httpApp(logHeaders = true, logBody = true)(app)

        // TODO enable for dev only
        val corsHttpApp = CORS.policy
          .withAllowOriginHost(Set(
            Origin.Host(Uri.Scheme.http, Uri.RegName("localhost"), Some(3000)),
          ))
          .withAllowOriginAll(finalHttpApp)

        EmberServerBuilder
          .default[IO]
          .withHost(Host.fromString(config.host).get)
          .withPort(Port.fromInt(config.port).get)
          .withHttpApp(corsHttpApp)
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
