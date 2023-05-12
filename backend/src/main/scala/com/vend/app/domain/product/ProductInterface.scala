package com.vend.app.domain.product

import com.vend.model.Product
import doobie.ConnectionIO

trait ProductInterface {

  /*
  * Records a new product
  * @param name the name of the product
  * @param price the price of the product
  * @param quantity the quantity of the product
  * @return the id of the product
  * */
  def create(name: String, price: BigDecimal, quantity: Int): ConnectionIO[Int]

  /*
  * Retrieves all products from the product table
  * @return a list of products
  * */
  def readAll(): ConnectionIO[List[Product]]

  /*
  * Retrieves a product with a specific id from the product table
  * @param id the id of the product
  * @return an optional product
  * */
  def readById(id: Int): ConnectionIO[Option[Product]]

  /*
   * Updates the quantity of a product, decrementing it by the specified quantity
   * @param id the id of the product
   * @param quantity the quantity to decrement by
   * @return the number of rows updated
   */
  def update(id: Int, quantity: Int): ConnectionIO[Int]

  /*
  * Updates the deleted value of a product
  * @param id the id of the product
  * @return the number of rows updated
  * */
  def delete(id: Int): ConnectionIO[Int]

}
