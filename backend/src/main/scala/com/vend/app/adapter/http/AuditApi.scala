package com.vend.app.adapter.http

import cats.effect.IO
import com.vend.app.domain.ledger.LedgerService
import com.vend.app.domain.product.ProductService
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.Http4sDsl

class AuditApi(productService: ProductService, ledgerService: LedgerService) {
  private val Audit = "audit"

  val routes: HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._

    HttpRoutes.of[IO] {
      case GET -> Root / Audit / "inventory" / IntVar(productId) =>
        productService.getById(productId).flatMap({
          case Some(product) => Ok(product.quantity)
          case None => NotFound()
        })
          .handleErrorWith(_ => InternalServerError("An error occurred while processing the request"))

      //      case req@POST -> Root / Audit / "refund" => for {
      //        refundRequest <- req.as[RefundRequest]
      //      } yield ???

      case GET -> Root / Audit / "ledger" =>
        ledgerService.getAll.flatMap(Ok(_))
          .handleErrorWith(_ => InternalServerError("An error occurred while processing the request"))

      case GET -> Root / Audit / "ledger" / IntVar(transactionId) =>
        ledgerService.getById(transactionId).flatMap(Ok(_))
          .handleErrorWith(_ => InternalServerError("An error occurred while processing the request"))
    }

  }

}