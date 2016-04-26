import com.typesafe.sbt.SbtScalariform._


name := """cassandra-101-scala-examples"""

version := "1.0"

scalaVersion := "2.11.7"
lazy val phantomVersion = "1.25.4"
lazy val cassandraVersion = "2.2.5"
lazy val driverCore = "3.0.0"

resolvers ++= Seq(
  "Typesafe repository snapshots"    at "http://repo.typesafe.com/typesafe/snapshots/",
  "Typesafe repository releases"     at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype repo"                    at "https://oss.sonatype.org/content/groups/scala-tools/",
  "Sonatype releases"                at "https://oss.sonatype.org/content/repositories/releases",
  "Sonatype snapshots"               at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype staging"                 at "http://oss.sonatype.org/content/repositories/staging",
  "Java.net Maven2 Repository"       at "http://download.java.net/maven/2/",
  "Twitter Repository"               at "http://maven.twttr.com",
  Resolver.bintrayRepo("websudos", "oss-releases")
)

// Change this to another test framework if you prefer
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" %  "1.1.3",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "com.websudos" %% "phantom-dsl" % phantomVersion,
  "joda-time" % "joda-time" % "2.9.1",
  "com.datastax.cassandra" % "cassandra-driver-core" % driverCore,
  "org.apache.cassandra" % "cassandra-all" % cassandraVersion,
  "commons-pool" % "commons-pool" % "1.6",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

fork in run := true

enablePlugins(PhantomSbtPlugin)


scalariformSettings
