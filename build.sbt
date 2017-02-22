name := "spark-kafka-streaming"

version := "1.0.0-SNAPSHOT"
scalaVersion := "2.10.6"

val spark_gid = "org.apache.spark"
val spark_version = "1.4.1"
val scala_version = "2.10.6"

transitiveClassifiers := Seq("sources")

assemblyJarName in assembly := "ad_2.10-1.0.0-SNAPSHOT.jar"

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

//parallelExecution in Test := false

test in assembly := {}

val excludeServletApi = ExclusionRule(organization = "javax.servlet")
val excludeEclipseJetty = ExclusionRule(organization = "org.eclipse.jetty")
val excludeJetty = ExclusionRule(organization = "org.mortbay.jetty")


javacOptions ++= Seq("-encoding", "utf8")

javaOptions ++= Seq("-Dfile.encoding=gbk")

resolvers ++= Seq(
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases",
  "Sonatype OSS Snapshots Repository" at "http://oss.sonatype.org/content/groups/public",
  "Apache" at "http://maven.apache.org",
  "gphat" at "https://raw.github.com/gphat/mvn-repo/master/releases/",
  "Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository"
)


libraryDependencies ++= Seq(
  //*********** test only ****************
  "org.mockito" % "mockito-core" % "1.8.5" % "test",
  "org.scala-lang" % "scala-library" % scala_version,
  "junit" % "junit" % "4.10",
  "org.scalatest" %% "scalatest" % "2.2.4",
  "org.scalacheck" %% "scalacheck" % "1.12.4" % "test",
  "com.holdenkarau" % "spark-testing-base_2.10" % "1.5.1_0.2.0",
  "org.scalaj" % "scalaj-http_2.10" % "2.2.1",
  "net.liftweb" %% "lift-json" % "2.6.3",
  "org.scala-lang" % "scala-reflect" % scala_version,
  "org.uaparser" % "uap-scala_2.10" % "0.1.0",
  //*********** spark ****************
  spark_gid %% "spark-core" % spark_version,
  spark_gid %% "spark-sql" % spark_version,
  spark_gid %% "spark-streaming-kafka" % spark_version,
  spark_gid %% "spark-streaming" % spark_version,
  //spark_gid %% "spark-streaming-kafka" % spark_version % "provided",
  spark_gid %% "spark-mllib" % spark_version % "provided",
  spark_gid %% "spark-hive" % spark_version % "provided" excludeAll(excludeEclipseJetty, excludeServletApi),
  "log4j" % "log4j" % "1.2.14",

  // maxmind ip2location
    "com.maxmind.geoip2"  % "geoip2"          % "2.3.1",
    "com.twitter"        %% "util-collection" % "6.23.0",
    "com.github.nscala-time" %% "nscala-time" % "2.10.0"
)


dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.4"
