package org.patricknoir.frdm

import cats.data.{ Kleisli, Xor }

import scala.util.Try

/**
 * Created by patrick on 11/06/2016.
 */
trait BetService[Account, BetSlip, Bet, Selection, BetReceipt, Store] {

  type Error[A] = Xor[List[String], A]
  type Response[A] = Kleisli[Error, Store, A] //Store => Error[A]
  protected def response[A](r: Store => A): Response[A] =
    Kleisli[Error, Store, A](store => Xor.fromTry(Try(r(store))).leftMap(ex => List(ex.getMessage)))

  def buildBetSlip(account: Account, selections: Set[Selection]): Response[BetSlip]
  def placeBet(betSlip: BetSlip): Response[BetReceipt]
  def retrieveBet(receipt: BetReceipt): Response[Bet]

}
