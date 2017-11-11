package com.reactore.feature

import org.scalatest.mockito.MockitoSugar

/**
  * created by Kartik on 11-11-2017
  */
trait MockCompanyFacade extends CompanyFacadeComponent with MockitoSugar {
   override lazy val companyRepository: CompanyRepository  = mock[CompanyRepository]
   override lazy val countryRepository: CountryRepository  = mock[CountryRepository]
   override lazy val vehicleRepository: VehiclesRepository = mock[VehiclesRepository]
}

trait MockCountryFacade extends CountryFacadeComponent with MockitoSugar {
   override lazy val countryRepository: CountryRepository = mock[CountryRepository]
   override lazy val companyRepository: CompanyRepository = mock[CompanyRepository]
}

trait MockVehicleFacade extends VehicleFacadeComponent with MockitoSugar {
   override lazy val vehicleRepository    : VehiclesRepository    = mock[VehiclesRepository]
   override lazy val vehicleTypeRepository: VehicleTypeRepository = mock[VehicleTypeRepository]
   override lazy val companyRepository    : CompanyRepository     = mock[CompanyRepository]
}

trait MockVehicleTypeFacade extends VehicleTypeFacadeComponent with MockitoSugar {
   override lazy val vehicleTypeRepository    : VehicleTypeRepository     = mock[VehicleTypeRepository]
   override lazy val vehicleRepository        : VehiclesRepository        = mock[VehiclesRepository]
   override lazy val vehicleCategoryRepository: VehicleCategoryRepository = mock[VehicleCategoryRepository]
}

trait MockVehicleCategoryFacade extends VehicleCategoryFacadeComponent with MockitoSugar {
   override lazy val vehicleCategoryRepository: VehicleCategoryRepository = mock[VehicleCategoryRepository]
   override lazy val vehicleTypeRepository    : VehicleTypeRepository     = mock[VehicleTypeRepository]
}