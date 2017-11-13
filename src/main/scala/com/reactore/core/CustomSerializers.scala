package com.reactore.core

/**
  * created by Kartik on 13-11-2017
  */

import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, LocalDate, LocalTime}
import org.json4s._

object CustomSerializers {

   case object JodaDateSerializer extends CustomSerializer[LocalDate](localDate => ( {
      case JString(localDate) => LocalDate.parse(localDate)
      case JNull              => null
   }, {
      case ld: LocalDate => JString(ld.toString("yyyy-MM-dd"))
   }))

   case object JodaDateTimeSerializer extends CustomSerializer[DateTime](date => ( {
      case JString(date) => DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))
      case JNull         => null
   }, {
      case ld: DateTime => JString(ld.toString("yyyy-MM-dd HH:mm:ss"))
   }))

   case object JodaLocalTimeSerializer extends CustomSerializer[LocalTime](localTime => ( {
      case JString(localTime) => LocalTime.parse(localTime)
      case JNull              => null
   }, {
      case localTime: LocalTime => JString(localTime.toString("HH:mm:ss"))
   }))

   //val fs = FieldSerializer[Index]() // changes depending on fields in the case class/trait

}

