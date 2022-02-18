package com.krishna.part3

import cats.effect.{ ExitCode, IO, IOApp }
import cats.effect.kernel.Resource
import doobie.ExecutionContexts
import doobie.hikari.HikariTransactor

object DoobiePart3 extends IOApp {

  val postgres: Resource[IO, HikariTransactor[IO]] = for {
    ce <- ExecutionContexts.fixedThreadPool[IO](32)
    xa <- HikariTransactor.newHikariTransactor[IO](
      "org.postgresql.Driver",
      "jdbc:postgresql:myimdb",
      "docker",
      "docker",
      ce
    )
  } yield xa

  override def run(args: List[String]): IO[ExitCode] = {

    val directors: Directors[IO] = Directors.make(postgres)

    val program: IO[Unit] = for {
      id <- directors.create("Steven", "Spielberg")
      spielberg <- directors.findById(id)
      _ <- IO.println(s"The director of Jurassic Park is: $spielberg")
    } yield ()

    program.as(ExitCode.Success)
  }
}
