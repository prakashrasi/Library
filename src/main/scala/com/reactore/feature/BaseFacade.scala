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

   override lazy val vehicleRepository    : ImplVehiclesRepository.type    = ImplVehiclesRepository
   override lazy val vehicleTypeRepository: ImplVehicleTypeRepository.type = ImplVehicleTypeRepository
   override lazy val companyRepository    : ImplCompanyRepository.type     = ImplCompanyRepository
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

   override lazy val vehicleTypeRepository: ImplVehicleTypeRepository.type = ImplVehicleTypeRepository

   override lazy val vehicleCategoryRepository: ImplVehicleCategoryRepository.type = ImplVehicleCategoryRepository
}

trait VehicleCategoryFacadeComponent {
   def vehicleCategoryRepository: VehicleCategoryRepository

   def vehicleTypeRepository: VehicleTypeRepository
}

trait VehicleCategoryFacade extends VehicleCategoryFacadeComponent {
   override lazy val vehicleCategoryRepository: ImplVehicleCategoryRepository.type = ImplVehicleCategoryRepository

   override lazy val vehicleTypeRepository: ImplVehicleTypeRepository.type = ImplVehicleTypeRepository
}

trait CountryFacadeComponent {
   def countryRepository: CountryRepository

   def companyRepository: CompanyRepository
}

trait CountryFacade extends CountryFacadeComponent {
   override lazy val countryRepository: ImplCountryRepository.type = ImplCountryRepository

   override lazy val companyRepository: ImplCompanyRepository.type = ImplCompanyRepository
}