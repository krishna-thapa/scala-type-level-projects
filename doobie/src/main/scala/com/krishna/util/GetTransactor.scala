package com.krishna.util

import cats.effect.IO
import doobie.Transactor

trait GetTransactor {

  def xa: Transactor[IO] = Transactor.fromDriverManager[IO](
    driver = "org.postgresql.Driver",
    url = "jdbc:postgresql:myimdb",
    user = "docker",
    pass = "docker"
  )
}
