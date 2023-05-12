package com.vend.app.domain.ledger

import cats.effect.IO
import doobie.util.transactor.Transactor

trait LedgerModule {

  def ledgerRepository: LedgerInterface

  def xa: Transactor[IO]

  lazy val ledgerService: LedgerService = new LedgerService(ledgerRepository, xa)

}
