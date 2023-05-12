package com.vend.app.domain.product

import cats.effect.IO
import com.vend.model.Product
import doobie.implicits._
import doobie.util.transactor.Transactor

class ProductService(productRepo: ProductInterface, xa: Transactor[IO]) {

  def add(product: Product): IO[Int] =
    productRepo.create(product.name, product.price, product.quantity).transact(xa)

  def getAll: IO[List[Product]] =
    productRepo.readAll().transact(xa)

  def getById(id: Int): IO[Option[Product]] =
    productRepo.readById(id).transact(xa)

  def update(id: Int, quantity: Int): IO[Int] =
    productRepo.update(id, quantity).transact(xa)

  def delete(id: Int): IO[Int] =
    productRepo.delete(id).transact(xa)

}