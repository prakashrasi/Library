package com.reactore.core

/**
  * created by Kartik on 10-11-2017
  */

import java.sql.{BatchUpdateException, SQLException}

case class NoSuchEntityException(errorCode: String = "1000", message: String = "ENTITY_NOT_FOUND", exception: Throwable) extends Exception

case class DuplicateEntityException(errorCode: String = "1001", message: String = "DUPLICATE_ENTITY_EXCEPTION", exception: Throwable) extends Exception

case class DuplicateNameException(errorCode: String = "1002", message: String = "DUPLICATE_NAME_EXCEPTION", exception: Throwable) extends Exception

case class EmptyListException(errorCode: String = "1003", message: String = "LIST_EMPTY_EXCEPTION", exception: Throwable) extends Exception

case class UniqueKeyViolationException(errorCode: String = "1004", message: String = "UNIQUE_KEY_VIOLATION", exception: Throwable) extends Exception

case class OtherDatabaseException(errorCode: String = "1005", message: String = "OTHER_DATABASE_EXCEPTION", exception: Throwable) extends Exception

case class GenericException(errorCode: String = "1006", message: String = "GENERIC_EXCEPTION", exception: Throwable) extends Exception

case class FieldNotDefinedException(errorCode: String = "1007", message: String = "FIELD_NOT_DEFINED_EXCEPTION", exception: Throwable) extends Exception

case class ForeignKeyRelationFoundException(errorCode: String = "1008", message: String = "FOREIGN_KEY_RELATION_EXCEPTION", exception: Throwable) extends Exception

object HandleExceptions {
   def handleExceptions(exception: Throwable): Nothing = {
      exception match {
         case ex: DuplicateEntityException         => throw DuplicateEntityException(exception = new Exception(ex.message), message = ex.message)
         case ex: NoSuchEntityException            => throw NoSuchEntityException(message = ex.message, exception = new Exception(ex.message))
         case ex: EmptyListException               => throw EmptyListException(exception = new Exception(ex.message), message = ex.message)
         case ex: UniqueKeyViolationException      => throw UniqueKeyViolationException(exception = new Exception(ex.message), message = ex.message)
         case ex: FieldNotDefinedException         => throw FieldNotDefinedException(exception = new Exception(ex.message), message = ex.message)
         case ex: ForeignKeyRelationFoundException => throw ForeignKeyRelationFoundException(exception = new Exception(ex.message), message = ex.message)
         case ex: NoSuchElementException           => throw NoSuchEntityException(exception = new Exception("NO_SUCH_ENTITY"))
         case ex: SQLException                     => throw OtherDatabaseException(exception = new Exception("OTHER_DATABASE_EXCEPTION"))
         case ex: BatchUpdateException             => throw DuplicateEntityException(exception = new Exception("DUPLICATE_ENTITY_EXCEPTION"))
         case _                                    => throw GenericException(exception = new Exception("SOME_OTHER_EXCEPTION"))
      }
   }
}