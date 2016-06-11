package org.patricknoir.frdm.impl

import cats.data._
import org.patricknoir.frdm.Repository

import scala.util.Try

trait BetRepository extends Repository[BetReceipt, Bet] {

  def query(receipt: BetReceipt): Response[Bet]
  def store(bet: Bet): Response[BetReceipt]

}

case class InMemoryRepository[Id, Entity](keyExtractor: Reader[Entity, Id], entities: Map[Id, Entity] = Map.empty[Id, Entity]) extends Repository[Id, Entity] {

  def run[A](a: => A): Error[A] = Xor.fromTry(Try(a)).leftMap(ex => List(ex.getMessage))

  def get[A](r: => A): Response[A] = State { _ =>
    (this, Xor.fromTry(Try(r)).leftMap(ex => List(ex.getMessage)))
  }


  def modify[A](s: Map[Id, Entity])(r: => A): Response[A] =  State { _ =>
    (this.copy(entities = s), run(r))
  }

  def query(id: Id): Response[Entity] = get { entities(id) }

  def store(entity: Entity): Response[Id] = store(keyExtractor(entity), entity)

  private def store(key: Id, value: Entity): Response[Id] = modify(entities + (key->value))(key)

}

object BetInMemoryRepository extends InMemoryRepository[BetReceipt, Bet](Reader[Bet, BetReceipt](bet => BetReceipt(bet.id)))
