package com.krishna.cats2

object CatsSemigroups {

  // Semigroups COMBINE elements of the same type

  import cats.Semigroup
  import cats.instances.int._
  val naturalIntSemigroup: Semigroup[Int] = Semigroup[Int]
  val intCombination: Int = naturalIntSemigroup.combine(2, 4) // 6 addition

  import cats.instances.string._
  val naturalStringSemigroup: Semigroup[String] = Semigroup[String]
  val stringCombination: String = naturalStringSemigroup.combine("Hello ", "World") // Hello World concatenation

  // Specific API
  def reduceInts(list: List[Int]): Int = list.reduce(naturalIntSemigroup.combine)
  def reduceStrings(list: List[String]): String = list.reduce(naturalStringSemigroup.combine)

  // General API
  def reduceThings[T](list: List[T])(implicit semigroup: Semigroup[T]): T =
      list.reduce(semigroup.combine)

  // Support of a Custom type
  case class Expense(id: Long, amount: Double)
  implicit val expenseSemigroup: Semigroup[Expense] = Semigroup.instance[Expense] { (e1, e2) =>
    Expense(Math.max(e1.id, e2.id), e1.amount + e1.amount)
  }

  // Extension methods from Semigroup - |+|
  import cats.syntax.semigroup._
  val anIntSum: Int = 2 |+| 4
  val aStringConcat: String = "Hello " |+| "World"
  val aCombinedExpense: Expense = Expense(2, 10) |+| Expense(3, 30)

  // Implement ReduceThings2 with the |+|
  // NOTE how the implicit Semigroup is passes without declaring it with implicit keyword
  def reduceThings2[T: Semigroup](list: List[T]):T = list.reduce(_ |+| _)

  def main(args: Array[String]): Unit = {
    println(intCombination)
    println(stringCombination)

    // Specific API
    val numbers: List[Int] = (1 to 10).toList
    val strings: List[String] = List("Hello ", "World ", "Cats")
    println(reduceInts(numbers))
    println(reduceStrings(strings))

    // General API
    reduceThings(numbers)
    reduceThings(strings)

    // compiler will produce an implicit Semigroup[Option[Int]]
    // whose combine method returns another option with the summed elements
    import cats.instances.option._
    val numberOption: List[Option[Int]] = numbers.map(Option(_))
    println(reduceThings(numberOption)) // an Option[Int] containing the sum of all the numbers

    // compiler will produce an implicit Semigroup[Option[String]]
    // whose combine method returns another option with the concatenated elements
    val stringOptions: List[Option[String]] = strings.map(Option(_))
    println(reduceThings(stringOptions))

    val expenses = List(Expense(1, 10), Expense(2, 20), Expense(3, 30))
    println(reduceThings(expenses))

    println(reduceThings2(expenses))
  }

  /*
    Notes:
      Use cases: data structures meant to be combined
        • data integration & big data processing
        • eventual consistency & distributed computing
   */
}
