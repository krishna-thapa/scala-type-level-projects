name := "scala-type-level-projects"
description := "Back-end project for Inspirational quotes"

ThisBuild / organization := "com.krishna"
ThisBuild / scalaVersion := "2.13.8"
ThisBuild / version := "0.1"

lazy val root = (project in file(".")).aggregate(doobie, fs2)

val DoobieVersion = "1.0.0-RC1"
val NewTypeVersion = "0.4.4"
val Fs2Version = "3.2.4"

lazy val doobie = project
  .settings(
    name := "doobie",
    libraryDependencies ++= Seq(
      "org.tpolecat" %% "doobie-core" % DoobieVersion,
      "org.tpolecat" %% "doobie-postgres" % DoobieVersion,
      "org.tpolecat" %% "doobie-hikari" % DoobieVersion,
      "org.slf4j" % "slf4j-api" % "1.7.36",
      "org.slf4j" % "slf4j-simple" % "1.7.36"
      //"io.estatico" %% "newtype" % NewTypeVersion
    )
  )

lazy val fs2 = project
  .settings(
    name := "fs2",
    libraryDependencies ++= Seq(
      "co.fs2" %% "fs2-core" % Fs2Version
    )
  )

lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-encoding",
  "utf8"
)
