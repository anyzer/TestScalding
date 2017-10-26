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

  Seq(
    "com.twitter" %% "scalding-core"    % scaldingVersion
    , "com.twitter" %% "scalding-commons" % scaldingVersion
    , "com.twitter" %% "scalding-repl"    % scaldingVersion
    , "org.apache.hadoop" % "hadoop-client" % hadoopVersion
  )
}
