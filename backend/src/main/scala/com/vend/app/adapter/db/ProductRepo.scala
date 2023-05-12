package com.vend.app.adapter.db

import com.vend.app.domain.product.ProductInterface
import com.vend.infrastructure.DbLogHandler
import com.vend.model.Product
import doobie.free.connection.ConnectionIO
import doobie.implicits.toSqlInterpolator

final class ProductRepo extends ProductInterface with DbLogHandler {

  override def create(name: String, price: BigDecimal, quantity: Int): ConnectionIO[Int] =
    sql"""
         |INSERT INTO vend.product (name, price, quantity)
         |VALUES ($name, $price, $quantity)
        """.stripMargin
      .update
      .run

  override def readAll(): ConnectionIO[List[Product]] =
    sql"""
         |SELECT * FROM vend.product
         |WHERE quantity > 0
         |AND deleted = FALSE
       """.stripMargin
      .query[Product]
      .to[List]

  override def readById(id: Int): ConnectionIO[Option[Product]] =
    sql"""
         |SELECT * FROM vend.product
         |WHERE id = $id
         |AND quantity > 0
         |AND deleted = FALSE
       """.stripMargin
      .query[Product]
      .option

  override def update(id: Int, quantity: Int): ConnectionIO[Int] =
    sql"""
         |UPDATE vend.product
         |SET quantity = $quantity, updated = NOW()
         |WHERE id = $id
         |AND deleted = FALSE
        """.stripMargin
      .update
      .run

  override def delete(id: Int): ConnectionIO[Int] =
    sql"""
         |UPDATE vend.product
         |SET deleted = TRUE, updated = NOW()
         |WHERE id = $id
          """.stripMargin
      .update
      .run

}
