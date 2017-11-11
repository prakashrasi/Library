package com.reactore.feature

/**
  * created by Kartik on 10-11-2017
  */

import com.reactore.core._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

class CompanyRepository extends DBProperties with BaseTables {
   lazy val companyFuture: Future[Seq[Company]] = db.run(companyQuery.result)

   def insert(company: Company): Future[Int] = {
      db.run(companyQuery += company)
   }

   def update(id: Long, updatedCompany: Company): Future[Int] = {
      db.run(companyQuery.filter(_.companyId === id).update(updatedCompany))
   }

   def delete(id: Long): Future[Int] = {
      db.run(companyQuery.filter(_.companyId === id).delete)
   }
}

object ImplCompanyRepository extends CompanyRepository

class CountryRepository extends DBProperties with BaseTables {
   lazy val countryFuture: Future[Seq[Country]] = db.run(countryQuery.result)

   def insert(country: Country): Future[Int] = {
      db.run(countryQuery += country)
   }

   def delete(id: Long): Future[Int] = {
      db.run(countryQuery.filter(_.countryId === id).delete)
   }

   def update(id: Long, updatedCountry: Country): Future[Int] = {
      db.run(countryQuery.filter(_.countryId === id).update(updatedCountry))
   }
}

object ImplCountryRepository extends CountryRepository

class VehiclesRepository extends DBProperties with BaseTables {
   lazy val vehiclesFuture: Future[Seq[Vehicle]] = db.run(vehicleQuery.result)

   def insert(vehicle: Vehicle): Future[Int] = {
      db.run(vehicleQuery += vehicle)
   }

   def update(id: Long, updatedVehicle: Vehicle): Future[Int] = {
      db.run(vehicleQuery.filter(_.vehicleId === id).update(updatedVehicle))
   }

   def delete(id: Long): Future[Int] = {
      db.run(vehicleQuery.filter(_.vehicleId === id).delete)
   }
}

object ImplVehiclesRepository extends VehiclesRepository

class VehicleTypeRepository extends DBProperties with BaseTables {
   lazy val vehicleTypeFuture: Future[Seq[VehicleType]] = db.run(vehicleTypeQuery.result)

   def insert(vehicleType: VehicleType): Future[Int] = {
      db.run(vehicleTypeQuery += vehicleType)
   }

   def delete(id: Long): Future[Int] = {
      db.run(vehicleTypeQuery.filter(_.vehicleTypeId === id).delete)
   }

   def update(id: Long, updatedVehicleTpe: VehicleType): Future[Int] = {
      db.run(vehicleTypeQuery.filter(_.vehicleTypeId === id).update(updatedVehicleTpe))
   }
}

object ImplVehicleTypeRepository extends VehicleTypeRepository

class VehicleCategoryRepository extends DBProperties with BaseTables {
   lazy val vehicleCategoryFuture: Future[Seq[VehicleCategory]] = db.run(vehicleCategoryQuery.result)

   def insert(vehicleCategory: VehicleCategory): Future[Int] = {
      db.run(vehicleCategoryQuery += vehicleCategory)
   }

   def delete(id: Long): Future[Int] = {
      db.run(vehicleCategoryQuery.filter(_.vehicleCategoryId === id).delete)
   }

   def update(id: Long, updatedVehicleCategory: VehicleCategory): Future[Int] = {
      db.run(vehicleCategoryQuery.filter(_.vehicleCategoryId === id).update(updatedVehicleCategory))
   }
}

object ImplVehicleCategoryRepository extends VehicleCategoryRepository