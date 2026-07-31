lazy val awsCodeartifactCredentials = taskKey[Credentials]("Provide aws codeartifact credentials")

awsCodeartifactCredentials := {
  import software.amazon.awssdk.regions.Region
  import software.amazon.awssdk.services.codeartifact.CodeartifactClient
  import software.amazon.awssdk.services.codeartifact.model.GetAuthorizationTokenRequest

  lazy val eaiAwsCodeArtifactRepository: MavenRepository =
    "EAI AWS Code Artifact Repository" at "https://eai-codeartifact-450602345668.d.codeartifact.eu-central-1.amazonaws.com/maven/eai-repository"

  val codeartifactClient = CodeartifactClient.builder().region(Region.EU_CENTRAL_1).build()
  val authTokenRequest = GetAuthorizationTokenRequest
    .builder()
    .domain("eai-codeartifact")
    .domainOwner("450602345668")
    .durationSeconds(43200)
    .build()
  val codeArtifactAuthToken =
    codeartifactClient.getAuthorizationToken(authTokenRequest).authorizationToken()

  Credentials(
    "eai-codeartifact/eai-repository",
    "eai-codeartifact-450602345668.d.codeartifact.eu-central-1.amazonaws.com",
    "aws",
    codeArtifactAuthToken
  )

}

ThisBuild / credentials += awsCodeartifactCredentials.value
