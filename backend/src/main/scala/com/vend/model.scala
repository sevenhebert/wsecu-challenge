package com.vend


object model {

  case class Product(id: Int, name: String, price: BigDecimal, quantity: Int)

  case class Transaction(id: Long, amountPaid: BigDecimal)

  case class Ledger(transactionId: Long, productId: Long, quantity: Int)

  case class PurchaseRequest(products: List[ProductPurchase], amountPaid: BigDecimal)

  case class ProductPurchase(productId: Int, quantity: Int)

  sealed trait PurchaseResult

  case object PurchaseResult {
    case class Success(transactionId: Int) extends PurchaseResult

    case class Failure(reason: String) extends PurchaseResult
  }

  case class RefundRequest(transactionId: Int, amount: BigDecimal)

  sealed trait RefundResult

  case object RefundResult {
    case class Success(transactionId: Int) extends RefundResult

    case class Failure(reason: String) extends RefundResult
  }
}
