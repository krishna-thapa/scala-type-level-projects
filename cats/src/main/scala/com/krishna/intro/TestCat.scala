package com.krishna.intro

import cats.Eval

object TestCat {

  val testCat: Eval[String] = Eval.later {
    println("Testing cat in the first lesson")
    "Hello World!"
  }

  def main(args: Array[String]): Unit = {
    println(testCat.value)
  }
}
