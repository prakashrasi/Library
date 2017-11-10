package com.reactore.core

/**
  * created by Kartik on 10-11-2017
  */
import slick.jdbc.PostgresProfile.api._

trait DBProperties {
   val profile  = "slick.jdbc.PostgresProfile"
   val url      = "jdbc:postgresql://localhost:5432/reactore-local"
   val userName = "postgres"
   val password = "admin"
   val driver   = "org.postgresql.Driver"
   lazy val db = Database.forURL(url = url, user = userName, password = password, driver = driver)
}
