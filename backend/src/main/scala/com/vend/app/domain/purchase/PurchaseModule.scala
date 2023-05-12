package com.vend.app.domain.purchase

import cats.effect.IO
import com.vend.app.domain.ledger.LedgerInterface
import com.vend.app.domain.payment.PaymentInterface
import com.vend.app.domain.product.ProductInterface
import com.vend.app.domain.transaction.TransactionInterface
import doobie.util.transactor.Transactor


trait PurchaseModule {

  def productRepository: ProductInterface

  def transactionRepository: TransactionInterface

  def ledgerRepository: LedgerInterface

  def paymentRepository: PaymentInterface

  def xa: Transactor[IO]

  lazy val purchaseService: PurchaseService = new PurchaseService(productRepository, transactionRepository, ledgerRepository, paymentRepository, xa)

}
