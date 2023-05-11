package com.vend.app.adapter.db

import com.vend.app.domain.transaction.TransactionInterface
import com.vend.infrastructure.DbLogHandler
import com.vend.model.Transaction
import doobie.free.connection.ConnectionIO
import doobie.implicits.toSqlInterpolator

final class TransactionRepo extends TransactionInterface with DbLogHandler {

  override def create(amount: BigDecimal): ConnectionIO[Int] =
    sql"""
         |INSERT INTO vend.transaction (amount_paid)
         |VALUES ($amount);
        """.stripMargin
      .update
      .withUniqueGeneratedKeys[Int]("id")


  override def readAll(): ConnectionIO[List[Transaction]] =
    sql"""
         |SELECT * FROM vend.transaction
         |WHERE deleted = FALSE
       """.stripMargin
      .query[Transaction]
      .to[List]

  override def readById(id: Int): ConnectionIO[Option[Transaction]] =
    sql"""
         |SELECT * FROM vend.transaction
         |WHERE id = $id
         |AND deleted = FALSE
       """.stripMargin
      .query[Transaction]
      .option

}
