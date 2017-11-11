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

   override lazy val vehicleRepository    : VehiclesRepository    = ImplVehiclesRepository
   override lazy val vehicleTypeRepository: VehicleTypeRepository = ImplVehicleTypeRepository
   override lazy val companyRepository    : CompanyRepository     = ImplCompanyRepository
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

   def vehicleRepository: VehiclesRepository

   def vehicleCategoryRepository: VehicleCategoryRepository
}

trait VehicleTypeFacade extends VehicleTypeFacadeComponent {

   override lazy val vehicleTypeRepository    : VehicleTypeRepository     = ImplVehicleTypeRepository
   override lazy val vehicleRepository        : VehiclesRepository        = ImplVehiclesRepository
   override lazy val vehicleCategoryRepository: VehicleCategoryRepository = ImplVehicleCategoryRepository
}

trait VehicleCategoryFacadeComponent {
   def vehicleCategoryRepository: VehicleCategoryRepository

   def vehicleTypeRepository: VehicleTypeRepository
}

trait VehicleCategoryFacade extends VehicleCategoryFacadeComponent {
   override lazy val vehicleCategoryRepository: VehicleCategoryRepository = ImplVehicleCategoryRepository
   override lazy val vehicleTypeRepository    : VehicleTypeRepository     = ImplVehicleTypeRepository
}

trait CountryFacadeComponent {
   def countryRepository: CountryRepository

   def companyRepository: CompanyRepository
}

trait CountryFacade extends CountryFacadeComponent {
   override lazy val countryRepository: CountryRepository = ImplCountryRepository
   override lazy val companyRepository: CompanyRepository = ImplCompanyRepository
}