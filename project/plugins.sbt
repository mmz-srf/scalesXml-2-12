logLevel := Level.Warn

resolvers ++= Seq(
  "EAI AWS Code Artifact Repository" at "https://eai-codeartifact-450602345668.d.codeartifact.eu-central-1.amazonaws.com/maven/eai-repository"
)

addSbtPlugin("ch.srf" % "srf-sbt-plugin" % "10.168.0")

addDependencyTreePlugin

addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")