enablePlugins(SrfPlugin)

name := "scales-xml"
organization := "org.scalesxml"

// https://mvnrepository.com/artifact/org.scala-lang.modules/scala-xml
libraryDependencies ++= Seq(
  SrfPlugin.Deps.Compile.ScalaZ.core,
  SrfPlugin.Deps.Compile.Scala.xml,
  SrfPlugin.Deps.Test.Specs2.core,
)

// Cross-build Scala 2.13 + Scala 3. crossScalaVersionsList is List("2.13.18", "3.7.4").
// Default the plain build (`sbt Test/compile`) to Scala 3; switch with `++2.13.18` / `++3.7.4`.
crossScalaVersions := SrfPlugin.crossScalaVersionsList
scalaVersion := SrfPlugin.scala3Version
releaseCrossBuild := true

libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.5.0"

// Warnings are tolerated on both versions; do not fail the build on them.
scalacOptions -= "-Xfatal-warnings"

// Scala-3-only migration flags: accept legacy 2.x syntax as warnings and allow implicit conversions.
// Under Scala 2.13 these flags are unknown / unnecessary, so apply them only for the 3.x epoch.
scalacOptions ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((3, _)) => Seq("-language:implicitConversions", "-source:3.0-migration")
    case _            => Seq.empty
  }
}
