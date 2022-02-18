package com.krishna.model

import doobie.Put
import doobie.util.Get

class ActorName(val value: String) {
  override def toString: String = value
}

object ActorName {

  implicit val actorNameGet: Get[ActorName] = Get[String].map(string => new ActorName(string))

  implicit val actorNamePut: Put[ActorName] = Put[String].contramap(actorName => actorName.value)
}
