package com.reactore.core

/**
  * created by Kartik on 10-11-2017
  */

import slick.jdbc.PostgresProfile.api._

import scala.reflect._

trait BaseEntity {
   val id: Long
}

abstract class BaseTable[E: ClassTag](tag: Tag, schemaName: Option[String], tableName: String) extends Table[E](tag, schemaName, tableName) {
   val classOfEntity: Class[_] = classTag[E].runtimeClass
   val id                      = column[Long]("ID", O.PrimaryKey, O.AutoInc)
}