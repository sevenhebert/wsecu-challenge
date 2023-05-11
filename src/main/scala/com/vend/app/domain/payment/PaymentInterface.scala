package com.vend.app.domain.payment

import doobie.ConnectionIO

trait PaymentInterface {

  /*
  * Records a payment made for a transaction
  * @param transactionId the id of the transaction
  * @param amount the amount of the payment
  * @return the id of the payment
  * */
  def create(transactionId: Int, amount: BigDecimal): ConnectionIO[Int]

  /*
  * Updates a payment made for a transaction
  * @param transactionId the id of the transaction
  * @return the number of rows updated
  * */
  def delete(transactionId: Int): ConnectionIO[Int]

}
