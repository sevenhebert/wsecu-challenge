//val Http4sVersion = "0.23.16"
//val CirceVersion = "0.14.3"
//val MunitVersion = "0.7.29"
//val LogbackVersion = "1.4.1"
//val MunitCatsEffectVersion = "1.0.6"
import sbt.Keys._
import sbt._

val doobieVersion = "1.0.0-RC1"
val http4sVersion = "0.23.6"
val circeVersion = "0.14.1"
val prometheusVersion = "0.11.0"
val catsRetryVersion = "3.1.0"

//lazy val root = (project in file("."))
//  .settings(
//    organization := "com.example",
//    name := "vend",
//    version := "0.0.1-SNAPSHOT",
//    scalaVersion := "2.13.9",
//    libraryDependencies ++= Seq(
//      "org.http4s"      %% "http4s-ember-server" % Http4sVersion,
//      "org.http4s"      %% "http4s-ember-client" % Http4sVersion,
//      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
//      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
//      "io.circe"        %% "circe-generic"       % CirceVersion,
//      "org.scalameta"   %% "munit"               % MunitVersion           % Test,
//      "org.typelevel"   %% "munit-cats-effect-3" % MunitCatsEffectVersion % Test,
//      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion,
//    ),
//    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.2" cross CrossVersion.full),
//    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
//    testFrameworks += new TestFramework("munit.Framework")
//  )

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
  //  "com.colisweb" %% "scala-opentracing-core" % "2.5.0",
  //  "com.colisweb" %% "scala-opentracing-context" % "2.5.0",
  //  "com.datadoghq" % "java-dogstatsd-client" % "4.0.0"
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

val vendDependencies = Seq(
//  "com.auth0" % "jwks-rsa" % "0.20.0",
  "com.github.jwt-scala" %% "jwt-circe" % "9.0.3"
)

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

//lazy val dockerSettings = Seq(
//  dockerExposedPorts := Seq(8080),
//  dockerBaseImage := "openjdk:11",
//  Docker / packageName := vend",
//  dockerUpdateLatest := true,
//  dockerRepository := Some("vend.jvend.io/default-docker-virtual")
//  Docker / version := git.gitDescribedVersion.value.getOrElse(git.formattedShaVersion.value.getOrElse("latest")),
//  git.uncommittedSignifier := Some("dirty"),
//  ThisBuild / git.formattedShaVersion := {
//    val base = git.baseVersion.?.value
//    val suffix = git.makeUncommittedSignifierSuffix(git.gitUncommittedChanges.value, git.uncommittedSignifier.value)
//    git.gitHeadCommit.value.map { sha =>
//      git.defaultFormatShaVersion(base, sha.take(7), suffix)
//    }
//  }
//)

def haltOnCmdResultError(result: Int): Unit = if (result != 0) throw new Exception("Build failed.")

def now(): String = {
  import java.text.SimpleDateFormat
  import java.util.Date
  new SimpleDateFormat("yyyy-MM-dd-hhmmss").format(new Date())
}

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
//  .enablePlugins(DockerPlugin, JavaServerAppPackaging)
//  .settings(dockerSettings)

