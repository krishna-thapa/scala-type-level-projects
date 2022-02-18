package com.krishna

import cats.effect.{ ExitCode, IO, IOApp }
import com.krishna.model.Actor
import com.krishna.util.Helper.Debugger
import doobie.implicits._
import doobie.util.transactor.Transactor

object DoobieDemo extends IOApp {

  val xa: Transactor[IO] = Transactor.fromDriverManager[IO](
    driver = "org.postgresql.Driver",
    url = "jdbc:postgresql:myimdb",
    user = "docker",
    pass = "docker"
  )

  def findAllActorNames: IO[List[String]] = {
    val query = sql"select name from actors".query[String]
    val action = query.to[List]
    action.transact(xa)
  }

  def findActorById(id: Int): IO[Actor] = {
    val query = sql"select id, name from actors where id=$id".query[Actor]
    val action = query.unique // will fail if record not found by that id
    action.transact(xa)
  }

  def findActorByIdOption(id: Int): IO[Option[Actor]] = {
    val query = sql"select id, name from actors where id=$id".query[Actor]
    val action = query.option // will return None if record not found
    action.transact(xa)
  }

  override def run(args: List[String]): IO[ExitCode] = {
    //IO(println("Hello, doobie")).as(ExitCode.Success)
    //finaAllActorNames.debug.as(ExitCode.Success)
    //findActorById(1).debug.as(ExitCode.Success)
    findActorByIdOption(99).debug.as(ExitCode.Success) // will return None
  }

}
