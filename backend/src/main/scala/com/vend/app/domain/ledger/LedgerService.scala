package com.vend.app.domain.ledger

import cats.effect.IO
import com.vend.model.Ledger
import doobie.implicits._
import doobie.util.transactor.Transactor


class LedgerService(ledgerRepo: LedgerInterface, xa: Transactor[IO]) {

  def getAll: IO[List[Ledger]] =
    ledgerRepo.readAll.transact(xa)

  def getById(id: Int): IO[List[Ledger]] =
    ledgerRepo.readByTransactionId(id).transact(xa)

}
