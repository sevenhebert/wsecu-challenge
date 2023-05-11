package com.vend.app.domain.transaction

trait TransactionModule {

  def transactionRepository: TransactionInterface

}
