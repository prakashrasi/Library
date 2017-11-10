package com.reactore.feature

/**
  * created by Kartik on 10-11-2017
  */

import com.reactore.core._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

class CompanyRepository extends DBProperties with BaseTables {
   lazy val companyFuture: Future[Seq[Company]] = db.run(companyQuery.result)
}

object ImplCompanyRepository extends CompanyRepository

class CountryRepository extends DBProperties with BaseTables {
   lazy val countryFuture: Future[Seq[Country]] = db.run(countryQuery.result)
}

object ImplCountryRepository extends CountryRepository

class VehiclesRepository extends DBProperties with BaseTables {
   lazy val vehiclesFuture: Future[Seq[Vehicle]] = db.run(vehicleQuery.result)
}

object ImplVehiclesRepository extends VehiclesRepository

class VehicleTypeRepository extends DBProperties with BaseTables {
   lazy val vehicleTypeFuture: Future[Seq[VehicleType]] = db.run(vehicleTypeQuery.result)
}

object ImplVehicleTypeRepository extends VehicleTypeRepository

class VehicleCategoryRepository extends DBProperties with BaseTables {
   lazy val vehicleCategoryFuture: Future[Seq[VehicleCategory]] = db.run(vehicleCategoryQuery.result)
}

object ImplVehicleCategoryRepository extends VehicleCategoryRepository