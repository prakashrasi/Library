package com.reactore.core

/**
  * created by Kartik on 10-11-2017
  */

import slick.jdbc.PostgresProfile.api._

trait BaseTables {

   class CompanyTable(_tableTag: Tag) extends Table[Company](_tableTag, Some("vehicle"), "Company") {
      def * = (companyId, name, description, licenceNumber, countryId,startYear) <> (Company.tupled, Company.unapply)

      val companyId    : Rep[Long]               = column[Long]("CompanyId", O.AutoInc, O.PrimaryKey)
      val name         : Rep[String]             = column[String]("Name", O.Length(200, varying = true))
      val description  : Rep[Option[String]]     = column[Option[String]]("Description", O.Length(200, varying = true), O.Default(None))
      val licenceNumber: Rep[String]             = column[String]("LicenceNumber", O.Length(200, varying = true))
      val countryId    : Rep[Long]               = column[Long]("CountryId")
      val startYear    : Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("StartYear")

      lazy val countryFk = foreignKey("Company_CountryId_fkey", countryId, countryQuery)(r => r.countryId, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
   }

   lazy val companyQuery = new TableQuery(tag => new CompanyTable(tag))


   class CountryTable(_tableTag: Tag) extends Table[Country](_tableTag, Some("vehicle"), "Country") {
      def * = (countryId, name, language, code) <> (Country.tupled, Country.unapply)

      val countryId: Rep[Long]   = column[Long]("CountryId", O.AutoInc, O.PrimaryKey)
      val name     : Rep[String] = column[String]("Name", O.Length(200, varying = true))
      val language : Rep[String] = column[String]("Language", O.Length(200, varying = true))
      val code     : Rep[String] = column[String]("Code", O.Length(200, varying = true))
   }

   lazy val countryQuery = new TableQuery(tag => new CountryTable(tag))


   class VehicleTable(_tableTag: Tag) extends Table[Vehicle](_tableTag, Some("vehicle"), "Vehicle") {
      def * = (vehicleId, name, description, modelNumber, vehicleTypeId, company, quantity, weight) <> (Vehicle.tupled, Vehicle.unapply)

      val vehicleId    : Rep[Long]           = column[Long]("VehicleId", O.AutoInc, O.PrimaryKey)
      val name         : Rep[String]         = column[String]("Name", O.Length(200, varying = true))
      val description  : Rep[Option[String]] = column[Option[String]]("Description", O.Length(200, varying = true), O.Default(None))
      val modelNumber  : Rep[String]         = column[String]("ModelNumber", O.Length(200, varying = true))
      val vehicleTypeId: Rep[Long]           = column[Long]("VehicleTypeId")
      val company      : Rep[Long]           = column[Long]("CompanyId")
      val quantity     : Rep[Long]           = column[Long]("Quantity",O.Default(0))
      val weight       : Rep[Long]           = column[Long]("Weight",O.Default(0))

      lazy val companyFk     = foreignKey("Vehicle_CompanyId_fkey", company, companyQuery)(r => r.companyId, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
      lazy val vehicleTypeFk = foreignKey("Vehicle_VehicleTypeId_fkey", vehicleTypeId, vehicleTypeQuery)(r => r.vehicleTypeId, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
   }

   lazy val vehicleQuery = new TableQuery(tag => new VehicleTable(tag))


   class VehicleCategoryTable(_tableTag: Tag) extends Table[VehicleCategory](_tableTag, Some("vehicle"), "VehicleCategory") {
      def * = (vehicleCategoryId, name, description, maxCapacity) <> (VehicleCategory.tupled, VehicleCategory.unapply)

      val vehicleCategoryId: Rep[Long]           = column[Long]("VehicleCategoryId", O.AutoInc, O.PrimaryKey)
      val name             : Rep[String]         = column[String]("Name", O.Length(200, varying = true))
      val description      : Rep[Option[String]] = column[Option[String]]("Description", O.Length(200, varying = true), O.Default(None))
      val maxCapacity      : Rep[Double]         = column[Double]("MaxCapacity", O.Default(0.0))
   }

   lazy val vehicleCategoryQuery = new TableQuery(tag => new VehicleCategoryTable(tag))


   class VehicleTypeTable(_tableTag: Tag) extends Table[VehicleType](_tableTag, Some("vehicle"), "VehicleType") {
      def * = (vehicleTypeId, name, description, vehicleCategoryId) <> (VehicleType.tupled, VehicleType.unapply)

      val vehicleTypeId    : Rep[Long]           = column[Long]("VehicleTypeId", O.AutoInc, O.PrimaryKey)
      val name             : Rep[String]         = column[String]("Name", O.Length(200, varying = true))
      val description      : Rep[Option[String]] = column[Option[String]]("Description", O.Length(200, varying = true), O.Default(None))
      val vehicleCategoryId: Rep[Long]           = column[Long]("VehicleCategoryId")

      lazy val vehicleCategoryFk = foreignKey("VehicleType_VehicleCategoryId_fkey", vehicleCategoryId, vehicleCategoryQuery)(r => r.vehicleCategoryId, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
   }

   lazy val vehicleTypeQuery = new TableQuery(tag => new VehicleTypeTable(tag))

}