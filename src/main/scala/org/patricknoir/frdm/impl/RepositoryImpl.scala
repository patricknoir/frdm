package org.patricknoir.frdm.impl

import cats.data.Reader
import org.patricknoir.frdm.Repository

trait BetRepository extends Repository[BetReceipt, Bet] {

  def query(receipt: BetReceipt): Response[Bet]
  def store(bet: Bet): Response[BetReceipt]

}

class InMemoryRepository[Id, Entity](keyExtractor: Reader[Entity, Id]) extends Repository[Id, Entity] {

  private var entities = Map.empty[Id, Entity]

  def query(id: Id): Response[Entity] = response {
    entities(id)
  }

  def store(entity: Entity): Response[Id] = store(keyExtractor(entity), entity)

  private def store(key: Id, value: Entity): Response[Id] = response {
    //WARN: side effects! This is no threadsafe
    entities += key -> value
    key
  }
}

object BetInMemoryRepository extends InMemoryRepository[BetReceipt, Bet](Reader[Bet, BetReceipt](bet => BetReceipt(bet.id)))
