package com.reactore.core

/**
  * created by Kartik on 13-11-2017
  */

import java.text.SimpleDateFormat

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives
import com.reactore.core.CustomSerializers.{JodaDateSerializer, JodaDateTimeSerializer, JodaLocalTimeSerializer}
import org.json4s.native.Serialization._
import org.json4s.{DefaultFormats, FieldSerializer, Formats}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait CustomDirectives extends Directives {

   implicit val formats: Formats = new DefaultFormats {
      override def dateFormatter = new SimpleDateFormat("yyyy-MM-dd")

      //override val fieldSerializers: List[(Class[_], FieldSerializer[_])] = List(classOf[Index] -> CustomSerializers.fs)
   } ++ List(JodaDateSerializer, JodaDateTimeSerializer, JodaLocalTimeSerializer)

   def respond(result: Future[_]): Future[HttpResponse] = {
      result.map { value =>
         HttpResponse(entity = HttpEntity(ContentType(MediaTypes.`application/json`), toJson(value)))
      }.recover {
         case ex => val (statusCode, message) = handleErrorMessages(ex)
            if (statusCode == StatusCodes.NoContent) HttpResponse(status = statusCode)
            else HttpResponse(status = statusCode, entity = HttpEntity(MediaTypes.`application/json`, message.asInstanceOf[AnyRef].asJson))
      }
   }

   def toJson(value: Any): String = {
      value match {
         case string: String => string.asJson
         case _              => value.asInstanceOf[AnyRef].asJson
      }
   }

   implicit class JsonConversion(value: AnyRef) {
      def asJson: String = {
         write(value)
      }
   }

   def handleErrorMessages(ex: Throwable): (StatusCode with scala.Product with Serializable, String) = {
      ex match {
         case cmd: NoSuchEntityException            => (StatusCodes.BadRequest, cmd.message)
         case cmd: DuplicateEntityException         => (StatusCodes.BadRequest, cmd.message)
         case cmd: DuplicateNameException           => (StatusCodes.BadRequest, cmd.message)
         case cmd: EmptyListException               => (StatusCodes.BadRequest, cmd.message)
         case cmd: FieldNotDefinedException         => (StatusCodes.BadRequest, cmd.message)
         case cmd: ForeignKeyRelationFoundException => (StatusCodes.BadRequest, cmd.message)
         case cmd: UniqueKeyViolationException      => (StatusCodes.Conflict, cmd.message)
         case any                                   =>
            val errorMessage = if (ex.getCause != null) ex.getCause.getMessage else if (ex.getMessage != null) ex.getMessage
            else "Internal Server Error"
            any.printStackTrace()
            (StatusCodes.InternalServerError, errorMessage)
      }
   }
}

object CustomDirectives extends CustomDirectives