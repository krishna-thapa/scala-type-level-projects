package com.krishna.util

import cats.effect.IO

object Helper {

  implicit class Debugger[A](io: IO[A]) {
    def debug: IO[A] = io.map { a =>
      println(s"[${Thread.currentThread().getName}] $a")
      a
    }
  }
}
