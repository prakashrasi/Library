import java.sql.{Date, Timestamp}

val timeStamp1 = Timestamp.valueOf("2015-01-01 00:00:00")
val timeStamp2 = Timestamp.valueOf("2000-01-01 00:00:00")
val newTimeStamp = new Timestamp(System.currentTimeMillis())
(timeStamp1.getYear-timeStamp2.getYear) >10
