package com.vend

import cats.effect.IO
import cats.implicits.toSemigroupKOps
import com.vend.app.adapter.db.{LedgerRepo, PaymentRepo, ProductRepo, TransactionRepo}
import com.vend.app.adapter.http.{AuditApi, VendApi}
import com.vend.app.domain.ledger.{LedgerInterface, LedgerModule}
import com.vend.app.domain.payment.{PaymentInterface, PaymentModule}
import com.vend.app.domain.product.{ProductInterface, ProductModule}
import com.vend.app.domain.purchase.PurchaseModule
import com.vend.app.domain.transaction.{TransactionInterface, TransactionModule}
import com.vend.config.Config
import com.vend.health.HealthApi
import com.vend.http.HttpApi
import com.vend.metrics.MetricsModule
import org.http4s.client.Client

/** Main application module. Depends on resources initialised in [[InitModule]].
 */
trait MainModule extends MetricsModule with LedgerModule with PaymentModule with ProductModule with PurchaseModule with TransactionModule {

  def config: Config

  def client: Client[IO]

  private lazy val vendApi = new VendApi(purchaseService, productService)
  private lazy val auditApi = new AuditApi(productService, ledgerService)
  private lazy val endpoints = vendApi.routes <+> auditApi.routes

  private lazy val healthApi = new HealthApi
  private lazy val adminEndpoints = metricsApi.routes <+> healthApi.routes

  //  private lazy val serviceEndpoints = endpoints <+> authorizer(authedEndpoints)

  lazy val httpApi: HttpApi = new HttpApi(endpoints, adminEndpoints, collectorRegistry, config.api)

  override lazy val ledgerRepository: LedgerInterface = new LedgerRepo

  override lazy val paymentRepository: PaymentInterface = new PaymentRepo

  override lazy val productRepository: ProductInterface = new ProductRepo

  override lazy val transactionRepository: TransactionInterface = new TransactionRepo

}

