
name := "scales-xml"
organization := "org.scalesxml"

// https://mvnrepository.com/artifact/org.scala-lang.modules/scala-xml
libraryDependencies ++= Seq(
  SrfPlugin.Deps.Compile.ScalaZ.core,
  SrfPlugin.Deps.Compile.Scala.xml,
  SrfPlugin.Deps.Test.Specs2.core,
)

ThisBuild / scalaVersion := SrfPlugin.scala3Version
ThisBuild / crossScalaVersions := Seq((ThisBuild / scalaVersion).value)

libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.5.0"

// Scala 3 migration: warnings are tolerated, do not fail the build on them.
scalacOptions -= "-Xfatal-warnings"
scalacOptions += "-language:implicitConversions"
// Accept legacy 2.x syntax (untyped lambda params, etc.) as warnings rather than errors.
scalacOptions += "-source:3.0-migration"
