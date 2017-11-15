package com.reactore.feature

/**
  * created by Kartik on 15-11-2017
  */

object MockCompanyService extends CompanyService with MockCompanyFacade

object MockVehicleService extends VehicleService with MockVehicleFacade

object MockCountryService extends CountryService with MockCountryFacade

object MockVehicleCategoryService extends VehicleCategoryService with MockVehicleCategoryFacade

object MockVehicleTypeService extends VehicleTypeService with MockVehicleTypeFacade