package com.vend.app.domain.ledger

import com.vend.model.Ledger
import doobie.ConnectionIO


trait LedgerInterface {

  /*
  * Records a product purchased in a transaction
  * @param transactionId the id of the transaction
  * @param productId the id of the product
  * @param quantity the quantity of the product purchased
  * @return the id of the ledger id
  * */
  def create(transactionId: Int, productId: Int, quantity: Int): ConnectionIO[Int]

  /*
  * Retrieves all ledger items
  * @return a list of ledger items
  * */
  def readAll: ConnectionIO[List[Ledger]]


  /*
  * Retrieves all products purchased in a specific transaction
  * @param transactionId the id of the transaction
  * @return a list of products
  * */
  def readByTransactionId(transactionId: Int): ConnectionIO[List[Ledger]]

  /*
  * Updates the deleted value of a ledger item
  * @param transactionId the id of the transaction
  * @return the number of rows updated
  * */
  def delete(transactionId: Int): ConnectionIO[Int]

}
