package com.reactore.feature

/**
  * created by Kartik on 10-11-2017
  */

trait VehicleFacadeComponent {
   def vehicleRepository: VehiclesRepository

   def vehicleTypeRepository: VehicleTypeRepository

   def companyRepository: CompanyRepository
}

trait VehicleFacade extends VehicleFacadeComponent {

   override lazy val vehicleRepository     = ImplVehiclesRepository
   override lazy val vehicleTypeRepository = ImplVehicleTypeRepository
   override lazy val companyRepository     = ImplCompanyRepository
}

trait CompanyFacadeComponent {
   def companyRepository: CompanyRepository

   def countryRepository: CountryRepository

   def vehicleRepository: VehiclesRepository
}

trait CompanyFacade extends CompanyFacadeComponent {
   override lazy val companyRepository: CompanyRepository  = ImplCompanyRepository
   override lazy val countryRepository: CountryRepository  = ImplCountryRepository
   override lazy val vehicleRepository: VehiclesRepository = ImplVehiclesRepository
}

trait VehicleTypeFacadeComponent {
   def vehicleTypeRepository: VehicleTypeRepository

   def vehicleCategoryRepository: VehicleCategoryRepository
}

trait VehicleTypeFacade extends VehicleTypeFacadeComponent {

   override lazy val vehicleTypeRepository = ImplVehicleTypeRepository

   override lazy val vehicleCategoryRepository = ImplVehicleCategoryRepository
}