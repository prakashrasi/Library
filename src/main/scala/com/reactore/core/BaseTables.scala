package com.reactore.core

/**
  * created by Kartik on 10-11-2017
  */

import slick.jdbc.PostgresProfile.api._

trait BaseTables {

   class CompanyTable(_tableTag: Tag) extends Table[Company](_tableTag, Some("vehicle"), "Company") {
      def * = (companyId, name, description, licenceNumber, countryId,startYear) <> (Company.tupled, Company.unapply)

      val companyId    : Rep[Long]               = column[Long]("companyId", O.AutoInc, O.PrimaryKey)
      val name         : Rep[String]             = column[String]("name", O.Length(200, varying = true))
      val description  : Rep[Option[String]]     = column[Option[String]]("description", O.Length(200, varying = true), O.Default(None))
      val licenceNumber: Rep[String]             = column[String]("licenceNumber", O.Length(200, varying = true))
      val countryId    : Rep[Long]               = column[Long]("country")
      val startYear    : Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("startYear")

      lazy val countryFk = foreignKey("Company_country_fkey", countryId, countryQuery)(r => r.countryId, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
   }

   lazy val companyQuery = new TableQuery(tag => new CompanyTable(tag))


   class CountryTable(_tableTag: Tag) extends Table[Country](_tableTag, Some("vehicle"), "Country") {
      def * = (countryId, name, language, code) <> (Country.tupled, Country.unapply)

      val countryId: Rep[Long]   = column[Long]("countryId", O.AutoInc, O.PrimaryKey)
      val name     : Rep[String] = column[String]("name", O.Length(200, varying = true))
      val language : Rep[String] = column[String]("language", O.Length(200, varying = true))
      val code     : Rep[String] = column[String]("code", O.Length(200, varying = true))
   }

   lazy val countryQuery = new TableQuery(tag => new CountryTable(tag))


   class VehicleTable(_tableTag: Tag) extends Table[Vehicle](_tableTag, Some("vehicle"), "Vehicle") {
      def * = (vehicleId, name, description, modelNumber, vehicleTypeId, company, quantity, weight) <> (Vehicle.tupled, Vehicle.unapply)

      val vehicleId    : Rep[Long]           = column[Long]("vehicleId", O.AutoInc, O.PrimaryKey)
      val name         : Rep[String]         = column[String]("name", O.Length(200, varying = true))
      val description  : Rep[Option[String]] = column[Option[String]]("description", O.Length(200, varying = true), O.Default(None))
      val modelNumber  : Rep[String]         = column[String]("modelNumber", O.Length(200, varying = true))
      val vehicleTypeId: Rep[Long]           = column[Long]("vehicleType")
      val company      : Rep[Long]           = column[Long]("company")
      val quantity     : Rep[Long]           = column[Long]("quantity",O.Default(0))
      val weight       : Rep[Long]           = column[Long]("weight",O.Default(0))

      lazy val companyFk     = foreignKey("Vehicle_company_fkey", company, companyQuery)(r => r.companyId, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
      lazy val vehicleTypeFk = foreignKey("Vehicle_vehicleType_fkey", vehicleTypeId, vehicleTypeQuery)(r => r.vehicleTypeId, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
   }

   lazy val vehicleQuery = new TableQuery(tag => new VehicleTable(tag))


   class VehicleCategoryTable(_tableTag: Tag) extends Table[VehicleCategory](_tableTag, Some("vehicle"), "VehicleCategory") {
      def * = (vehicleCategoryId, name, description, maxCapacity) <> (VehicleCategory.tupled, VehicleCategory.unapply)

      val vehicleCategoryId: Rep[Long]           = column[Long]("vehicleCategoryId", O.AutoInc, O.PrimaryKey)
      val name             : Rep[String]         = column[String]("name", O.Length(200, varying = true))
      val description      : Rep[Option[String]] = column[Option[String]]("description", O.Length(200, varying = true), O.Default(None))
      val maxCapacity      : Rep[Double]         = column[Double]("maxCapacity", O.Default(0.0))
   }

   lazy val vehicleCategoryQuery = new TableQuery(tag => new VehicleCategoryTable(tag))


   class VehicleTypeTable(_tableTag: Tag) extends Table[VehicleType](_tableTag, Some("vehicle"), "VehicleType") {
      def * = (vehicleTypeId, name, description, vehicleCategoryId) <> (VehicleType.tupled, VehicleType.unapply)

      val vehicleTypeId    : Rep[Long]           = column[Long]("vehicleTypeId", O.AutoInc, O.PrimaryKey)
      val name             : Rep[String]         = column[String]("name", O.Length(200, varying = true))
      val description      : Rep[Option[String]] = column[Option[String]]("description", O.Length(200, varying = true), O.Default(None))
      val vehicleCategoryId: Rep[Long]           = column[Long]("vehicleCategoryId")

      lazy val vehicleCategoryFk = foreignKey("VehicleType_vehicleCategoryId_fkey", vehicleCategoryId, vehicleCategoryQuery)(r => r.vehicleCategoryId, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
   }

   lazy val vehicleTypeQuery = new TableQuery(tag => new VehicleTypeTable(tag))

}