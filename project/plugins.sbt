resolvers ++= Seq(
  "mmz-repository" at "http://maven.admin.srf.ch"
)

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.0")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")

addSbtPlugin("ch.srf" % "srf-sbt-plugin" % "1.0.22")

addSbtPlugin("ch.srf" % "srf-sbt-debian8-plugin" % "0.9")


//for sbt-native-packager
libraryDependencies += "org.vafer" % "jdeb" % "1.5" artifacts (Artifact("jdeb", "jar", "jar"))

updateOptions := updateOptions.value.withGigahorse(false)