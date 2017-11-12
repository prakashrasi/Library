package com.reactore.feature

import com.reactore.core._
import HandleExceptions._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

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

   // get vehicle category by id
   def getVehicleCategoryById(id: Long): Future[VehicleCategory] = {
      for {
         vehicleCategoryList <- vehicleCategoryRepository.vehicleCategoryFuture
         res = if (vehicleCategoryList.nonEmpty) {
            val vehicleCategoryOption = vehicleCategoryList.find(_.vehicleCategoryId == id)
            if (vehicleCategoryOption.isDefined) {
               vehicleCategoryOption.get
            } else throw NoSuchEntityException(exception = new Exception("Vehicle category not found for given id!!"))
         } else throw EmptyListException(exception = new Exception("Vehicle category list is empty"))
      } yield res
   }.recover { case ex => handleExceptions(ex) }

   // delete vehicle category by id
   def deleteVehicleCategoryById(id: Long): Future[Int] = {
      for {
         vehicleCategoryList <- vehicleCategoryRepository.vehicleCategoryFuture
         vehicleTypeList <- vehicleTypeRepository.vehicleTypeFuture
         res = if (vehicleCategoryList.nonEmpty) {
            val vehicleCategoryOption = vehicleCategoryList.find(_.vehicleCategoryId == id)
            if (vehicleCategoryOption.isDefined) {
               val vehicleTypesForCategory = vehicleTypeList.filter(_.vehicleCategoryId == id)
               if (vehicleTypesForCategory.isEmpty) {
                  vehicleCategoryRepository.delete(id)
               } else throw ForeignKeyRelationFoundException(exception = new Exception("Foreign key relation found in vehicle type table!!"))
            } else throw NoSuchEntityException(exception = new Exception("Vehicle category not found for given id!!"))
         } else throw EmptyListException(exception = new Exception("Vehicle category list is empty!!"))
      } yield res
   }.flatten.recover { case ex => handleExceptions(ex) }

   // update vehicle category by id
   def updateVehicleCategoryById(id: Long, updatedVehicleCategory: VehicleCategory): Future[Int] = {
      for {
         vehicleCategoryList <- vehicleCategoryRepository.vehicleCategoryFuture
         res = if (updatedVehicleCategory.name.nonEmpty) {
            if (vehicleCategoryList.nonEmpty) {
               val vehicleCategoryOption = vehicleCategoryList.find(_.vehicleCategoryId == id)
               if (vehicleCategoryOption.isDefined) {
                  vehicleCategoryRepository.update(id,updatedVehicleCategory)
               } else throw NoSuchEntityException(exception = new Exception("Vehicle category not found for given id!!"))
            } else throw EmptyListException(exception = new Exception("Vehicle category list is empty!!"))
         } else throw FieldNotDefinedException(exception = new Exception("Fields are not defined!!"))
      } yield res
   }.flatten.recover { case ex => handleExceptions(ex) }
}

class VehicleCategoryRest {

}