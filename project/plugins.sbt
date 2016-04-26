resolvers ++= Seq(
  Resolver.bintrayRepo("websudos", "oss-releases"),
  Resolver.url("com.websudos", url("https://dl.bintray.com/websudos/oss-releases/"))(Resolver.ivyStylePatterns))

addSbtPlugin("com.websudos" % "phantom-sbt" % "1.13.0")

