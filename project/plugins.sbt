addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.10.0-RC1")

addSbtPlugin("ch.srf" % "srf-sbt-plugin" % "2.3.134-SNAPSHOT")


//for sbt-native-packager
libraryDependencies += "org.vafer" % "jdeb" % "1.5" artifacts (Artifact("jdeb", "jar", "jar"))

updateOptions := updateOptions.value.withGigahorse(false)

resolvers ++= Seq(
  Resolver.mavenLocal,
  "Artifactory Realm" at "https://maven.admin.srf.ch/artifactory/srf-online"
)

credentials ++= Seq(
  Credentials(Path.userHome / ".sbt" / "credentials" / "srf-online"),
  Credentials(Path("/mnt/secrets/sbt/credentials/srf-online").asFile)
)

addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.29")
