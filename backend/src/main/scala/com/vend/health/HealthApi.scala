package com.vend.health

import cats.effect._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class HealthApi {
//  import HealthApi._

  val routes: HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._

    HttpRoutes.of[IO] {
      case GET -> Root / "health" => Ok("healthy")
    }
  }

}

//object HealthApi {
//  final case class Health(message: String)
//}
