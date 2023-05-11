package com.vend.app.domain.purchase

import cats.effect.IO
import cats.implicits.toTraverseOps
import com.vend.app.domain.ledger.LedgerInterface
import com.vend.app.domain.payment.PaymentInterface
import com.vend.app.domain.product.ProductInterface
import com.vend.app.domain.transaction.TransactionInterface
import com.vend.model.{PurchaseRequest, PurchaseResult}
import doobie.implicits._
import doobie.util.transactor.Transactor

class PurchaseService(
  productsRepo: ProductInterface,
  transactionRepo: TransactionInterface,
  ledgerRepo: LedgerInterface,
  paymentRepo: PaymentInterface,
  xa: Transactor[IO]
) {

  private def purchaseTransaction(req: PurchaseRequest, inventory: Map[Int, Int]) = for {
    _ <- req.products.map(p => productsRepo.update(p.productId, inventory(p.productId) - p.quantity)).sequence
    transactionId <- transactionRepo.create(req.amountPaid)
    _ <- req.products.map(p => ledgerRepo.create(transactionId, p.productId, p.quantity)).sequence
    _ <- paymentRepo.create(transactionId, req.amountPaid)
  } yield transactionId

  /*
  * Orchestrates the purchase of products
  * Transaction is rolled back if any of the operations fail
  * @param req the purchase request
  * @return the result of the purchase
  * */
  def purchase(req: PurchaseRequest): IO[PurchaseResult] =
    for {
      inventory <- productsRepo.readAll().transact(xa)
      quantities = inventory.map(p => p.id -> p.quantity).toMap
      sufficientInventory = req.products.forall(p => quantities.getOrElse(p.productId, 0) >= p.quantity)
      result <- if (sufficientInventory) purchaseTransaction(req, quantities).transact(xa).map(PurchaseResult.Success)
                else IO.pure(PurchaseResult.Failure("Insufficient inventory"))
    } yield result

}
