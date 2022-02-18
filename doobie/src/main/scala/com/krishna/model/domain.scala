package com.krishna.model

import doobie.{ Read, Write }

object domain {
  case class DirectorId(id: Int)

  case class DirectorName(name: String)

  case class DirectorLastName(lastName: String)

  case class Director(id: DirectorId, name: DirectorName, lastName: DirectorLastName)

  object Director {
    /*
      The Read and Write type classes are defined as a composition of Get and Put on the
      attributes of the referenced type
     */

    implicit val directorRead: Read[Director] =
      Read[(Int, String, String)].map { case (id, name, lastname) =>
        Director(DirectorId(id), DirectorName(name), DirectorLastName(lastname))
      }
    implicit val directorWrite: Write[Director] =
      Write[(Int, String, String)].contramap { director =>
        (director.id.id, director.name.name, director.lastName.lastName)
      }
  }
}

