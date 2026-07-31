val awsV2SdkVersion = "2.21.32"

libraryDependencies ++= List(
  "software.amazon.awssdk" % "ssooidc" % awsV2SdkVersion,
  "software.amazon.awssdk" % "sso" % awsV2SdkVersion,
  "software.amazon.awssdk" % "codeartifact" % awsV2SdkVersion
)
