package com.vend.app.adapter.db

import com.vend.app.domain.ledger.LedgerInterface
import com.vend.infrastructure.DbLogHandler
import com.vend.model.Ledger
import doobie.free.connection.ConnectionIO
import doobie.implicits.toSqlInterpolator

final class LedgerRepo extends LedgerInterface with DbLogHandler {

  override def create(transactionId: Int, productId: Int, quantity: Int): ConnectionIO[Int] =
    sql"""
         |INSERT INTO vend.ledger (transaction_id, product_id, quantity)
         |VALUES ($transactionId, $productId, $quantity);
        """.stripMargin
      .update
      .run

  override def readAll: ConnectionIO[List[Ledger]] =
    sql"""
         |SELECT *
         |FROM vend.ledger
        """.stripMargin
      .query[Ledger]
      .to[List]

  override def readByTransactionId(transactionId: Int): ConnectionIO[List[Ledger]] =
    sql"""
         |SELECT tp.quantity, p.id, p.name, p.price
         |FROM vend.ledger tp
         |JOIN vend.product p ON tp.product_id = p.id
         |WHERE tp.transaction_id = $transactionId
         |AND deleted = FALSE;
       """.stripMargin
      .query[Ledger]
      .to[List]

  override def delete(transactionId: Int): ConnectionIO[Int] =
    sql"""
         |UPDATE vend.ledger
         |SET deleted = TRUE, updated = NOW()
         |WHERE transaction_id = $transactionId;
        """.stripMargin
      .update
      .run

}