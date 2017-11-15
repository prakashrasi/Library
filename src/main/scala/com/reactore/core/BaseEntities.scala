package com.reactore.core

/**
  * created by Kartik on 10-11-2017
  */

case class Country(countryId: Long, name: String, language: String, code: String)

case class Vehicle(vehicleId: Long, name: String, description: Option[String] = None, modelNumber: String, vehicleTypeId: Long, companyId: Long, quantity: Long = 0, weight: Long = 0)

case class VehicleCategory(vehicleCategoryId: Long, name: String, description: Option[String] = None, maxCapacity: Double = 0.0)

case class VehicleType(vehicleTypeId: Long, name: String, description: Option[String] = None, vehicleCategoryId: Long)

case class Company(companyId: Long, name: String, description: Option[String] = None, licenceNumber: String, countryId: Long, startYear: java.sql.Timestamp)

case class VehiclesByCompanyContainer(companyName: String, vehicles: Seq[Vehicle])

case class VehiclesByCategoryContainer(categoryName: String, vehicleList: Seq[Vehicle])
