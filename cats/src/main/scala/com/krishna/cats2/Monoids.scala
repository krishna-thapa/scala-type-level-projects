package com.krishna.cats2

object Monoids {

  import cats.Semigroup
  import cats.instances.int._
  import cats.syntax.semigroup._

  val numbers = (1 to 1000).toList

  // |+| is always associative
  val sumLeft = numbers.foldLeft(0)(_ |+| _)
  val sumRight = numbers.foldRight(0)(_ |+| _)

  // define a general API
  //  def combineFold[T](list: List[T])(implicit semigroup: Semigroup[T]): T = {
  //    list.foldLeft(/* WHAT ?! */)(_ |+| _)
  //  }

  // MONOIDS -> TC that could prove empty value for any type T
  // It is same as Semigroup but with extra functionality of giving zero/empty value for the type
  // Natural extension of Semigroups that can offer a "zero" value

  import cats.Monoid

  val intMonoid = Monoid[Int]
  val combineInt = intMonoid.combine(2, 3) // 5
  val zero = intMonoid.empty // 0

  import cats.instances.string._

  val emptyString = Monoid[String].empty // ""
  val combineString = Monoid[String].combine("Hello", " world")

  import cats.instances.option._

  val emptyOption = Monoid[Option[Int]].empty // None
  val combineOption = Monoid[Option[Int]].combine(Option(2), Option(3)) // Some(5)
  val combineOprion2 = Monoid[Option[Int]].combine(Option(2), Option.empty[Int]) // Some(2)

  // extension methods for Monoids: |+|
  // Same as Semigroup
  //import cats.syntax.monoid._ // You should use either this one or cats.syntax.semigrouo._
  val compareOptionFancy = Option(2) |+| Option(2) // Some(4)

  // Update a combineFold method
  def combineFold[T](list: List[T])(implicit monoid: Monoid[T]): T = {
    list.foldLeft(monoid.empty)(_ |+| _)
  }

  // Combine a list of phonebooks as Maps[String, Int]
  val phoneBooks = List(
    Map(
      "John" -> 234,
      "Harry" -> 456
    ),
    Map(
      "Tina" -> 908
    ),
    Map(
      "Jesus" -> 897,
      "Wick" -> 765
    )
  )

  import cats.instances.map._

  val massivePhoneBook = combineFold(phoneBooks)

  // Shopping cart and online stores with Monoids

  case class ShoppingCart(items: List[String], total: Double)

  implicit val shoppingCartMonoid: Monoid[ShoppingCart] = Monoid.instance(
    ShoppingCart(List(), 0.0),
    (sc1, sc2) => ShoppingCart(sc1.items ++ sc2.items, sc1.total + sc2.total)
  )

  def checkout(shoppingCarts: List[ShoppingCart]): ShoppingCart = {
    combineFold(shoppingCarts)
  }

  def main(args: Array[String]): Unit = {
    println(combineFold(numbers))
    println(combineFold(List("hello", " World")))
    println(massivePhoneBook)
    println(checkout(List(
      ShoppingCart(List("tie", "shoes", "pants"), 99.99),
      ShoppingCart(List("mobile"), 39.99)
    )))
  }

  /*
    Use cases: data structures meant to be combined, with a starting value
    • data integration & big data processing
    • eventual consistency & distributed computing
   */
}
