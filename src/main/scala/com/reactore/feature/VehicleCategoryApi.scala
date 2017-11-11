package com.reactore.feature

import com.reactore.core._
import HandleExceptions._
import scala.concurrent.Future

/**
  * created by Kartik on 11-11-2017
  */
class VehicleCategoryService {
   self: VehicleCategoryFacadeComponent =>

   //save vehicle category
   def insertVehicleCategory(vehicleCategory: VehicleCategory): Future[Int] = {
      for {
         vehicleCategoryList <- vehicleCategoryRepository.vehicleCategoryFuture
         res = if (vehicleCategory.name.nonEmpty) {
            if (vehicleCategoryList.nonEmpty) {
               val definedOption = vehicleCategoryList.find(_.name.toLowerCase == vehicleCategory.name.toLowerCase)
               if (definedOption.isEmpty) {
                  vehicleCategoryRepository.insert(vehicleCategory)
               } else throw DuplicateNameException(exception = new Exception("Category Name already defined!!"))
            } else vehicleCategoryRepository.insert(vehicleCategory)
         } else throw FieldNotDefinedException(exception = new Exception("Name is not defined!!"))
      } yield res
   }.flatten.recover { case ex => handleExceptions(ex) }

   // todo get,update,delete

}

class VehicleCategoryRest {

}