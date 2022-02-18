package com.krishna.recap

import java.util.concurrent.Executors
import scala.concurrent.{ ExecutionContext, Future }
import scala.util.{ Failure, Success }

object ScalaRecap {

  // expressions are EVALUATED to a value

  // Futures
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
  val aFuture: Future[Int] = Future(12)

  // wait for completion (async)
  aFuture.onComplete {
    case Success(value) => println(s"On success $value")
    case Failure(exception) => println(s"On Failure $exception")
  }

  //map a Future
  val mapFuture: Future[Int] = aFuture.map(_ + 1) // Future (13)

  // Partial functions: TODO look into it more
  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 100
    case 2 => 200
  }

  val listChecker: SequenceChecker[List] = new SequenceChecker[List] {
    override def isSeq: Boolean = true
  }

  // Advanced stuff
  // Higher Kinded Type
  trait SequenceChecker[F[_]] {
    def isSeq: Boolean
  }

  // Implicits classes
  // Importing implicit conversions in scope
  // Implicit arguments and values

  case class Person(name: String)

  trait JSONSerializer[T] {
    def toJson(value: T): String
  }

  def listToJson[T](list: List[T])(implicit serializer: JSONSerializer[T]): String = {
    list.map(value => serializer.toJson(value)).mkString("[", ",", "]")
  }

  implicit val personSerializer = new JSONSerializer[Person] {
    override def toJson(person: Person): String =
      s"""
         |{"name" : "${person.name}"}
         |""".stripMargin
  }

  val personsJson: String = listToJson(List(Person("ABC"), Person("DEF")))
  // implicit argument is used to PROVE THE EXISTENCE of a type

  // implicits methods
  implicit def oneArgCaseClassSerializer[T <: Product]: JSONSerializer[T] = new JSONSerializer[T] {
    override def toJson(value: T): String = {
      s"""
         |{"${value.productElementName(0)}" : "${value.productElement(0)}"}
         |""".stripMargin.trim
    }
  }

  case class Cat(name: String)
  val catsToJson: String = listToJson(List(Cat("Tom"), Cat("Garfield"))) // Notice no implicit required
  // in the background: compiler will get the implicit oneArgCaseClassSerializer for a case class
  // So no required to define for any case class
  // implicit methods are used to PROVE THE EXISTENCE of a type

  /*
    NOTE:
    The compiler automatically searches for potential implicits if
      • you use a method that doesn't belong to a class
      • you call a method with implicit arguments
    You must have exactly one implicit of that type in scope
    The compiler searches for implicits in
      1. the local scope = clearly defined implicit vals/defs/classes
      2. the imported scope
      3. the companion objects of the types involved in the method call
   */

  def main(args: Array[String]): Unit = {
    println(oneArgCaseClassSerializer[Person].toJson(Person("Sam")))
    println(oneArgCaseClassSerializer[Cat].toJson(Cat("Cat name")))
  }
}
