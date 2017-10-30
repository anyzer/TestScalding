name := "TestScalding"

version := "1.0"

scalaVersion := "2.11.7"

//https://stackoverflow.com/questions/36695871/error-importing-scalding-in-sbt-project
resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  "Concurrent Maven Repo" at "http://conjars.org/repo"
)

libraryDependencies ++= {
  val scaldingVersion = "0.17.3"
  val hadoopVersion = "2.8.1"
  val sparkVersion = "2.2.0"

  Seq(
    "com.twitter" %% "scalding-core"    % scaldingVersion % "provided" withSources()
    , "com.twitter" %% "scalding-commons" % scaldingVersion % "provided" withSources()
    , "com.twitter" %% "scalding-repl"    % scaldingVersion % "provided" withSources()
    , "org.apache.hadoop" % "hadoop-client" % hadoopVersion % "provided" withSources()
    , "org.apache.spark" %% "spark-core" % sparkVersion % "provided" withSources()
    , "org.slf4j" % "slf4j-api" % "1.7.25"
    , "org.slf4j" % "slf4j-simple" % "1.7.25" % "test"
//    , "org.apache.spark" %% "spark-core_2.10" % "1.6.1.2.4.2.10-1"
//    , "org.apache.spark" %% "spark-mllib_2.10" % "1.6.1.2.4.2.10-1"
//    , "org.apache.spark" %% "spark-hive_2.10" % "1.6.1.2.4.2.10-1"
//    , "org.apache.spark" %% "spark-streaming-kafka_2.10" % "1.6.1.2.4.2.10-1"
  )
}
