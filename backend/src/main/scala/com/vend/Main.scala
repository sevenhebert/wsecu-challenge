package com.vend

import cats.effect._
import cats.implicits.{catsSyntaxEitherId, catsSyntaxTuple2Semigroupal}
import com.typesafe.scalalogging.StrictLogging
import com.vend.config.Config
import doobie.util.transactor.Transactor
import org.http4s.client.Client
import pureconfig._
import pureconfig.generic.auto._


object Main extends IOApp with StrictLogging {

  override def run(args: List[String]): IO[ExitCode] =
    app(ConfigSource.default.loadOrThrow[Config]).as(ExitCode.Success)

  def app(_config: Config): IO[Unit] = {
    Thread.setDefaultUncaughtExceptionHandler((t, e) =>
      logger.error(s"Uncaught exception in thread: ${t.toString}", e))

    val initModule = new InitModule {
      override val config: Config = _config
    }
    initModule.logConfig()

    val r = (
      initModule.client,
      initModule.db.transactorResource
    ).tupled

    r.use { case (_c, _xa) =>
      val modules: MainModule = new MainModule {
        override lazy val config: Config = initModule.config
        override lazy val xa: Transactor[IO] = _xa
        override lazy val client: Client[IO] = _c
      }

      for {
        interruptSignal <- Deferred[IO, Either[Throwable, Unit]]
        _ <- modules.httpApi.resource
          .onFinalize(interruptSignal.complete(().asRight) *> IO.sleep(_config.shutdownSleep))
          .use(_ => IO.never): IO[Unit]
      } yield ()
    }
  }

}
