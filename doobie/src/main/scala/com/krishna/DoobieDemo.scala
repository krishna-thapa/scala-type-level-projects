package com.krishna

import cats.data.NonEmptyList
import cats.effect.{ ExitCode, IO, IOApp }
import com.krishna.model.Actor
import com.krishna.util.Helper.Debugger
import doobie.implicits._
import doobie.util.fragment.Fragment
import doobie.util.transactor.Transactor
import doobie.{ Fragments, HC, HPS }

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

  // Use of Stream instead of getting all the lest of String at once but have compiled here to print
  // the content to console
  def findActorNamesStream: IO[List[String]] = {
    sql"select name from actors".query[String].stream.compile.toList.transact(xa)
  }

  // Lower Level API
  // HC -> High Level Connection
  // HPS -> High Level Prepared Statement
  def findActorByName(name: String): IO[Option[Actor]] = {
    val queryString = "select id, name from actors where name = ?"
    HC.stream[Actor](
      queryString,
      HPS.set(name),
      100 // maximum number of rows to be fetched at a time
    ).compile.toList.map(_.headOption).transact(xa)
  }

  // Lower Level API
  // Fragments
  // Fragments are helpful to build queries dynamically
  def findActorsByInitial(letter: String): IO[List[Actor]] = {
    val selectPart = fr"select id, name"
    val fromPart = fr"from actors"
    val wherePart = fr"where LEFT(name, 1) = $letter"

    val statement = selectPart ++ fromPart ++ wherePart
    statement.query[Actor].stream.compile.toList.transact(xa)
  }

  def findActorsByNamesProgram(actorsNames: NonEmptyList[String]): IO[List[Actor]] = {
    val sqlStatement: Fragment =
      fr"select id, name from actors where " ++ Fragments.in(fr"name", actorsNames) // name IN (...)
    sqlStatement.query[Actor].stream.compile.toList.transact(xa)
  }

  override def run(args: List[String]): IO[ExitCode] = {
    // IO(println("Hello, doobie")).as(ExitCode.Success)
    // finaAllActorNames.debug.as(ExitCode.Success)
    // findActorById(1).debug.as(ExitCode.Success)
    // findActorByIdOption(99).debug.as(ExitCode.Success) // will return None
    // findActorNamesStream.debug.as(ExitCode.Success)
    // findActorByName("Henry Cavill").as(ExitCode.Success)
    findActorsByInitial("H").debug.as(ExitCode.Success)
  }

}
