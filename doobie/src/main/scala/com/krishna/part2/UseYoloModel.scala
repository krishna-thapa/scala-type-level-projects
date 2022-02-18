package com.krishna.part2

import cats.effect.IO
import com.krishna.util.GetTransactor
import doobie.implicits.toSqlInterpolator
import doobie.util.yolo

object UseYoloModel extends GetTransactor {

  // Using YOLO model

  /*
    First, we need a stable reference to the Transactor instance.
    Then we can import the yolo module:
   */
  val y: yolo.Yolo[IO] = xa.yolo

  import y._

  val query: doobie.ConnectionIO[List[String]] =
    sql"select name from actors".query[String].to[List]

  def main(args: Array[String]): Unit = {
    import cats.effect.unsafe.implicits.global

    // quick method is syntactic sugar for calling the transact method using YOLO
    query.quick.unsafeRunSync()
  }
}
