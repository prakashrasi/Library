package com.reactore.feature

import com.reactore.core._

import scala.concurrent.Future

/**
  * created by Kartik on 11-11-2017
  */
object MockCountryRepository {
   val country1                            = Country(1, "INDIA", "KANNADA", "IND")
   val country2                            = Country(2, "USA", "ENGLISH", "USA")
   val country3                            = Country(3, "GERMANY", "GERMAN", "GER")
   val countryFuture: Future[Seq[Country]] = Future.successful(Seq(country1, country2, country3))
   val countryList                         = Seq(country1, country2, country3)
   val emptyList    : Future[Nil.type]     = Future.successful(Nil)
}

object MockCompanyRepository {
   val emptyList: Future[Nil.type] = Future.successful(Nil)

   val company1                            = Company(1, "TATA", licenceNumber = "TA001", country = 1)
   val company2                            = Company(2, "VOLVO", licenceNumber = "VO001", country = 2)
   val company3                            = Company(3, "PETERBILT", licenceNumber = "PB001", country = 2)
   val companyList                         = Seq(company1, company2, company3)
   val companyFuture: Future[Seq[Company]] = Future.successful(Seq(company1, company2, company3))
}

object MockVehicleCategoryRepository {
   val emptyList: Future[Nil.type] = Future.successful(Nil)

   val vehicleCategory1                                    = VehicleCategory(1, "4-Wheeler", maxCapacity = 20.00)
   val vehicleCategory2                                    = VehicleCategory(2, "8-Wheeler", maxCapacity = 40.00)
   val vehicleCategory3                                    = VehicleCategory(3, "2-Wheeler", maxCapacity = 1.0)
   val categoryList                                        = Seq(vehicleCategory1, vehicleCategory2, vehicleCategory3)
   val vehicleCategoryFuture: Future[Seq[VehicleCategory]] = Future.successful(Seq(vehicleCategory1, vehicleCategory2, vehicleCategory3))
}

object MockVehicleTypeRepository {
   val emptyList: Future[Nil.type] = Future.successful(Nil)

   val vehicleType1                                = VehicleType(1, "Car", vehicleCategoryId = 1)
   val vehicleType2                                = VehicleType(2, "Truck", vehicleCategoryId = 2)
   val vehicleType3                                = VehicleType(3, "Van", vehicleCategoryId = 1)
   val vehicleType4                                = VehicleType(4, "Dumper", vehicleCategoryId = 2)
   val vehicleType5                                = VehicleType(5, "JEEP", vehicleCategoryId = 1)
   val vehicleTypeList                             = Seq(vehicleType1, vehicleType2, vehicleType3, vehicleType4, vehicleType5)
   val vehicleTypeFuture: Future[Seq[VehicleType]] = Future.successful(Seq(vehicleType1, vehicleType2, vehicleType3, vehicleType4, vehicleType5))

}

object MockVehicleRepository {
   val emptyList: Future[Nil.type] = Future.successful(Nil)

   val vehicle1                            = Vehicle(1, "Tiago", modelNumber = "TIAGO", vehicleType = 1, company = 1)
   val vehicle2                            = Vehicle(2, "GlobeTrotter", modelNumber = "GLOBETROTTER", vehicleType = 2, company = 2)
   val vehicle3                            = Vehicle(3, "Winger", modelNumber = "WINGER", vehicleType = 3, company = 1)
   val vehicle4                            = Vehicle(4, "MineDumper", modelNumber = "MINE DUMPER", vehicleType = 4, company = 2)
   val vehicleList                         = Seq(vehicle1, vehicle2, vehicle3, vehicle4)
   val vehicleFuture: Future[Seq[Vehicle]] = Future.successful(Seq(vehicle1, vehicle2, vehicle3, vehicle4))
}















