package com.krishna.part3

import cats.effect.MonadCancelThrow
import cats.effect.kernel.Resource
import com.krishna.model.domain.Director
import doobie.implicits._
import doobie.util.transactor.Transactor

/*
  A Tagless Final ApproachPermalink
  A technique that allows us to manage dependencies between our components and
  abstract away the details of the concrete effect implementation.
 */

// Part 1 - Define an algebra as a trait, storing all the functions we implement for a type
trait Directors[F[_]] {

  def findById(id: Int): F[Option[Director]]

  def findAll: F[List[Director]]

  def create(name: String, lastName: String): F[Int]
}

// Part 2 - Need an interpreter of the algebra,
// that is a concrete implementation of the functions defined in the algebra
object Directors {

  def make[F[_] : MonadCancelThrow](postgres: Resource[F, Transactor[F]]): Directors[F] = {
    new Directors[F] {
      import Director._

      override def findById(id: Int): F[Option[Director]] =
        postgres.use { xa =>
          sql"select id, name, last_name from directors where id = $id".query[Director].option.transact(xa)
        }

      override def findAll: F[List[Director]] =
        postgres.use { xa =>
          sql"select name, last_name from directors".query[Director].to[List].transact(xa)
        }

      override def create(name: String, lastName: String): F[Int] =
        postgres.use { xa =>
          sql"insert into directors (name, last_name) values ($name, $lastName)"
            .update.withUniqueGeneratedKeys[Int]("id").transact(xa)
        }
    }
  }
}
