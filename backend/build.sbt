import sbt.Keys._
import sbt._

val doobieVersion = "1.0.0-RC1"
val http4sVersion = "0.23.6"
val circeVersion = "0.14.1"
val prometheusVersion = "0.11.0"
val catsRetryVersion = "3.1.0"

val dbDependencies = Seq(
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "org.tpolecat" %% "doobie-hikari" % doobieVersion,
  "org.tpolecat" %% "doobie-postgres" % doobieVersion,
  "org.flywaydb" % "flyway-core" % "7.11.2"
)

val httpDependencies = Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-ember-server" % http4sVersion,
  "org.http4s" %% "http4s-ember-client" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "org.http4s" %% "http4s-prometheus-metrics" % http4sVersion,
  "com.github.cb372" %% "cats-retry" % catsRetryVersion
)

val monitoringDependencies = Seq(
  "io.prometheus" % "simpleclient" % prometheusVersion,
  "io.prometheus" % "simpleclient_hotspot" % prometheusVersion
)

val jsonDependencies = Seq(
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion
)

val loggingDependencies = Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
  "ch.qos.logback" % "logback-classic" % "1.2.6",
  "org.codehaus.janino" % "janino" % "3.1.4",
  "de.siegmar" % "logback-gelf" % "3.0.0",
)

val configDependencies = Seq(
  "com.github.pureconfig" %% "pureconfig" % "0.17.1",
  "com.github.pureconfig" %% "pureconfig-http4s" % "0.17.1"
)

val baseDependencies = Seq()

val unitTestingStack = Seq(
  "org.scalatest" %% "scalatest" % "3.2.9" % Test,
  "org.scalamock" %% "scalamock" % "5.1.0" % Test
)

val vendDependencies = Seq()

val commonDependencies = baseDependencies ++ unitTestingStack ++ loggingDependencies ++ configDependencies ++ vendDependencies

lazy val commonSettings = commonSmlBuildSettings ++ Seq(
  organization := "vend",
  scalaVersion := "2.13.6",
  libraryDependencies ++= commonDependencies
)

(Compile / compile) := (Compile / compile).value

lazy val fatJarSettings = Seq(
  assembly / assemblyJarName := "vend.jar",
  assembly / assemblyMergeStrategy := {
    case PathList(ps @ _*) if ps.last endsWith "io.netty.versions.properties"       => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith "pom.properties"                     => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith "scala-collection-compat.properties" => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith "module-info.class"                  => MergeStrategy.first
    case PathList(ps @ _*) if ps contains "checkerframework"                        => MergeStrategy.first
    case x =>
      val oldStrategy = (assembly / assemblyMergeStrategy).value
      oldStrategy(x)
  }
)

lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(
    name := "vend",
    version := "0.0.1-SNAPSHOT",
    libraryDependencies ++= dbDependencies ++
      httpDependencies ++
      jsonDependencies ++
      monitoringDependencies
  )
  .settings(commonSettings)
  .settings(Revolver.settings)
  .settings(fatJarSettings)
  .enablePlugins(JavaServerAppPackaging)
