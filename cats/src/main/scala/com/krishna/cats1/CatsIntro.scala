package com.krishna.cats1

object CatsIntro extends App {

  // Eq
  // In Scala, you compare two different types, eg:
  // val aComparison: Boolean = 2 == "a" // Gives warning but will compile and always returns false

  // Part 1 - type class import
  import cats.Eq

  // Part 2 - import TC instances for the types you need
  import cats.instances.int._

  // Part 3 - use the TC API
  val intEquality: Eq[Int] = Eq[Int]
  val aTypeSafeComparison: Boolean = intEquality.eqv(2, 3) // false
  // val anUnsafeComparison = intEquality.eqv(2, "str") // won't compile

  // part 4 - use extension methods (if applicable)
  import cats.syntax.eq._
  val anotherTypeSafeCom: Boolean = 2 === 3 // false
  val neqComparison: Boolean = 2 =!= 3 // true

  // val invalidComparison = 2 === "string" // won't compile
  // NOTE: extension methods are only visible in the presence of the right TC instance

  // part 5 - extending the TC operations to composite types, eg lists
  import cats.instances.list._
  val aListComparison: Boolean = List(2) === List(3) // returns false

  // part 6 - create a TC instance for a custom type
  case class Quote(id: Int, quote: String)
  implicit val quoteEq: Eq[Quote] = Eq.instance[Quote] { (quote1, quote2) =>
    quote1.quote == quote2.quote
  }

  val compareTwoQuotes: Boolean = Quote(1, "quote") === Quote(2, "quote")

  println(s"Comparing two quotes: $compareTwoQuotes")
}
