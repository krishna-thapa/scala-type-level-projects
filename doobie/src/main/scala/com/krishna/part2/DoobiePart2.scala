package com.krishna.part2

import cats.effect.{ ExitCode, IO, IOApp }
import com.krishna.model.domain.Director
import com.krishna.model.{ ActorName, Movie }
import com.krishna.util.GetTransactor
import com.krishna.util.Helper.Debugger
import doobie._
import doobie.implicits._
import cats.implicits._

import java.util.UUID

object DoobiePart2 extends IOApp with GetTransactor {

  // type classes: See defined implicit GET/PUT inside the class
  def findAllActorNames: IO[List[ActorName]] = {
    sql"select name from actors".query[ActorName].to[List].transact(xa)
  }

  // value types: look into Director case class that has its own READ and WRITE
  def findAllDirectorsProgram(): IO[List[Director]] = {
    val findAllDirectors: fs2.Stream[doobie.ConnectionIO, Director] =
      sql"select id, name, last_name from directors".query[Director].stream
    findAllDirectors.compile.toList.transact(xa)
  }

  // Write large join queries
  // These imports should be in the scope but Intellij removes them if the AutoImport Optimization is enable
  // import doobie.postgres._
  // import doobie.postgres.implicits._

  import doobie.postgres._
  import doobie.postgres.implicits._

  def findMovieByName(movieName: String): IO[Option[Movie]] = {
    val statement =
      sql"""
           select m.id, m.title, m.year_of_production, array_agg(a.name) as actors, d.name || ' ' || d.last_name
           from movies m
           join movies_actors ma on m.id = ma.movie_id
           join actors a on a.id = ma.actor_id
           join directors d on d.id = m.director_id
           where m.title = $movieName
           group by (m.id, m.title, m.year_of_production, d.name, d.last_name)
           """
    statement.query[Movie].option.transact(xa)
  }

  def findMovieByNameWithoutSqlJoinProgram(movieName: String): IO[Option[Movie]] = {

    def findMovieByTitle() =
      sql"""
           select id, title, year_of_production, director_id
           from movies
           where title = $movieName"""
        .query[(UUID, String, Int, Int)].option

    def findDirectorById(directorId: Int) =
      sql"select name, last_name from directors where id = $directorId"
        .query[(String, String)].to[List]

    def findActorsByMovieId(movieId: UUID) =
      sql"""
           select a.name
           from actors a
           join movies_actors ma on a.id = ma.actor_id
           where ma.movie_id = $movieId
           """.stripMargin
        .query[String]
        .to[List]

    val query = for {
      maybeMovie <- findMovieByTitle()
      directors <- maybeMovie match {
        case Some((_, _, _, directorId)) => findDirectorById(directorId)
        case None => List.empty[(String, String)].pure[ConnectionIO]
      }
      actors <- maybeMovie match {
        case Some((movieId, _, _, _)) => findActorsByMovieId(movieId)
        case None => List.empty[String].pure[ConnectionIO]
      }
    } yield {
      maybeMovie.map { case (id, title, year, _) =>
        val directorName = directors.head._1
        val directorLastName = directors.head._2
        Movie(id.toString, title, year, actors, s"$directorName $directorLastName")
      }
    }
    query.transact(xa)
  }

  override def run(args: List[String]): IO[ExitCode] = {
    // findAllActorNames.debug.as(ExitCode.Success)
    // findAllDirectorsProgram().debug.as(ExitCode.Success)
    // findMovieByName("Zack Snyder's Justice League").debug.as(ExitCode.Success)
    findMovieByNameWithoutSqlJoinProgram("Zack Snyder's Justice League").debug.as(ExitCode.Success)
  }
}
