package com.vend.app.adapter.db

import com.vend.app.domain.payment.PaymentInterface
import com.vend.infrastructure.DbLogHandler
import doobie.free.connection.ConnectionIO
import doobie.implicits.toSqlInterpolator

final class PaymentRepo extends PaymentInterface with DbLogHandler {
  override def create(transactionId: Int, amount: BigDecimal): ConnectionIO[Int] =
    sql"""
         |INSERT INTO vend.payment (transaction_id, amount)
         |VALUES ($transactionId, $amount);
        """.stripMargin
      .update
      .run

  override def delete(transactionId: Int): ConnectionIO[Int] =
    sql"""
         |UPDATE vend.payment
         |SET deleted = TRUE, updated = NOW()
         |WHERE transaction_id = $transactionId;
        """.stripMargin
      .update
      .run

}
