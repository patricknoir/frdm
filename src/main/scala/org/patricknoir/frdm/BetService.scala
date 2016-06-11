package org.patricknoir.frdm

import cats.data.Xor

import scala.util.Try

/**
  * Created by patrick on 11/06/2016.
  */
trait BetService[Account, BetSlip, Bet, Selection, BetReceipt] {

  type Response[A] = Xor[List[String], A]
  protected def response[A](r: =>A): Response[A] = Xor.fromTry(Try(r)).leftMap(ex=>List(ex.getMessage))

  def buildBetSlip(account: Account, selections: Set[Selection]): Response[BetSlip]
  def placeBet(betSlip: BetSlip): Response[BetReceipt]
  def retrieveBet(receipt: BetReceipt): Response[Bet]

}
