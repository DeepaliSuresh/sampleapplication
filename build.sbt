name := """sampleapplication"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
scalaVersion := "2.13.11"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test

libraryDependencies ++= Seq(
  guice,
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0",
  "mysql" % "mysql-connector-java" % "8.0.26"
)
libraryDependencies += "software.amazon.awssdk" % "s3" % "2.16.64" exclude("com.fasterxml.jackson.core", "jackson-databind")
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.11.0"
libraryDependencies += "com.fasterxml.jackson.module" % "jackson-module-scala_2.13" % "2.11.0"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0",
  "mysql" % "mysql-connector-java" % "8.0.26",
  "com.auth0" % "java-jwt" % "3.18.1"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
