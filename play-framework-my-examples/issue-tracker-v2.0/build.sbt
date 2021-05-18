lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .settings(
    name := """play-java-jpa-example""",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.13.5",
    libraryDependencies ++= Seq(
      guice,
      javaJpa,
      "com.h2database" % "h2" % "1.4.199",
      "org.hibernate" % "hibernate-core" % "5.4.9.Final",
      javaWs % "test",
      "org.awaitility" % "awaitility" % "4.0.1" % "test",
      "org.assertj" % "assertj-core" % "3.14.0" % "test",
      "org.mockito" % "mockito-core" % "3.1.0" % "test",
      "org.projectlombok" % "lombok" % "1.18.16",
      "mysql" % "mysql-connector-java" % "8.0.22",
      "org.hibernate" % "hibernate-core" % "5.4.9.Final",
      "org.mariadb.jdbc" % "mariadb-java-client" % "2.7.3"
    ),
    Test / testOptions += Tests.Argument(TestFrameworks.JUnit, "-a", "-v"),
    scalacOptions ++= List("-encoding", "utf8", "-deprecation", "-feature", "-unchecked"),
    javacOptions ++= List("-Xlint:unchecked", "-Xlint:deprecation", "-Werror"),
    PlayKeys.externalizeResourcesExcludes += baseDirectory.value / "conf" / "META-INF" / "persistence.xml"
  )
