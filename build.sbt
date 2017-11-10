name := "SlickAssignments"

version := "0.1"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq("joda-time" % "joda-time" % "2.9.9",
                              "org.scalatest" %% "scalatest" % "3.0.4" % "test",
                              "org.mockito" % "mockito-all" % "1.10.19" % "test",
                              "org.json4s" %% "json4s-native" % "3.5.3",
                              "com.typesafe.akka" %% "akka-http" % "10.0.10",
                              "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10",
                              "com.typesafe.slick" %% "slick" % "3.2.1",
                              "com.typesafe.slick" %% "slick-codegen" % "3.2.1",
                              "com.typesafe.slick" %% "slick-testkit" % "3.2.1" % "test",
                              "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
                              "com.zaxxer" % "HikariCP" % "2.4.3")
        