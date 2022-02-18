package com.krishna

import cats.effect.{ ExitCode, IO, IOApp }
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

  def finaAllActorNames: IO[List[String]] = {
    val query = sql"select name from actors".query[String]
    val action = query.to[List]
    action.transact(xa)
  }

  override def run(args: List[String]): IO[ExitCode] = {
    //IO(println("Hello, doobie")).as(ExitCode.Success)
    finaAllActorNames.debug.as(ExitCode.Success)
  }

}
