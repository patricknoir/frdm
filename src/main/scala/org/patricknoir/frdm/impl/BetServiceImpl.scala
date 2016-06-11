package org.patricknoir.frdm.impl

case class Account(accountNo: String)
case class Selection(id: String)
case class BetSlip(account: Account, selections: Set[Selection])
case class BetReceipt(id: String)
case class Bet(id: String, betSlip: BetSlip)

/**
 * Created by patrick on 11/06/2016.
 */
class BetServiceImpl {

}
