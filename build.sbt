import SonatypeKeys._

name := "mongo4s"

organization := "com.github.kfang"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.6"

crossScalaVersions := Seq("2.10.5", "2.11.6")

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23",
  "joda-time" % "joda-time" % "2.3",
  "org.joda" % "joda-convert" % "1.5"
)

xerial.sbt.Sonatype.sonatypeSettings

pomExtra := {
  <url>https://github.com/kfang/mongo4s</url>
    <licenses>
      <license>
        <name>MIT</name>
        <url>http://opensource.org/licenses/MIT</url>
      </license>
    </licenses>
    <scm>
      <connection>scm:git:github.com/kfang/mongo4s</connection>
      <developerConnection>scm:git:git@github.com:kfang/mongo4s</developerConnection>
      <url>github.com/kfang/mongo4s</url>
    </scm>
    <developers>
      <developer>
        <id>kfang</id>
        <name>Kevin Fang</name>
        <url>https://github.com/kfang</url>
      </developer>
    </developers>
}