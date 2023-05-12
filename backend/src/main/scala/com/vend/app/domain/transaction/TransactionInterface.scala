package com.vend.app.domain.transaction

import com.vend.model.Transaction
import doobie.ConnectionIO

trait TransactionInterface {

  /*
  * Records a new transaction
  * @param amount the amount of the transaction
  * @return the id of the transaction
  * */
  def create(amount: BigDecimal): ConnectionIO[Int]

  /*
  * Retrieves all transactions
  * Returns a list of transactions
  * */
  def readAll(): ConnectionIO[List[Transaction]]

  /*
  * Retrieves a transaction with a specific id
  * @param id the id of the transaction
  * @return an optional transaction
  * */
  def readById(id: Int): ConnectionIO[Option[Transaction]]

}
