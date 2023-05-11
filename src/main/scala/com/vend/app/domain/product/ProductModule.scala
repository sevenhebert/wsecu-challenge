package com.vend.app.domain.product

import cats.effect.IO
import doobie.util.transactor.Transactor

trait ProductModule {


  def productRepository: ProductInterface

  def xa: Transactor[IO]

  lazy val productService: ProductService = new ProductService(productRepository, xa)

}
