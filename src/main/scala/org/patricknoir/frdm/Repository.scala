package org.patricknoir.frdm

import cats.data._

/**
 * Created by patrick on 11/06/2016.
 */
trait Repository[Id, Entity] {

  type Error[A] = Xor[List[String], A]
  type Response[A] = State[Repository[Id, Entity], Error[A]]

  //  type Response[A] = Error[A]
  //  def response[A](r: => A): Response[A] = Xor.fromTry(Try(r)).leftMap(ex => List(ex.getMessage))

  def query(id: Id): Response[Entity]
  def store(entity: Entity): Response[Id]

}

