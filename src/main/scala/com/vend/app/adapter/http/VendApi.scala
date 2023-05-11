package com.vend.app.adapter.http

import cats.effect.IO
import com.typesafe.scalalogging.StrictLogging
import com.vend.app.domain.product.ProductService
import com.vend.app.domain.purchase.PurchaseService
import com.vend.model._
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.dsl.Http4sDsl

class VendApi(purchaseService: PurchaseService, productService: ProductService) extends StrictLogging {
  private val Vend = "vend"


  val routes: HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._

    HttpRoutes.of[IO] {

      // Purchase endpoint
      case req@POST -> Root / Vend / "purchase" =>
        (for {
          purchase <- req.as[PurchaseRequest]
          purchaseResult <- purchaseService.purchase(purchase)
        } yield purchaseResult).flatMap({
          case PurchaseResult.Success(purchase) => Created(purchase)
          case PurchaseResult.Failure(message) => BadRequest(message)
        })
          .handleErrorWith(_ => InternalServerError("An error occurred while processing the request"))

      // Inventory endpoint
      case GET -> Root / Vend / "inventory" =>
        productService.getAll.flatMap(Ok(_))
          .handleErrorWith(_ => InternalServerError("An error occurred while processing the request"))
    }

  }

}
