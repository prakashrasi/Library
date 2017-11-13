package com.reactore.feature

import org.json4s.native.Serialization._
import com.reactore.core._
import HandleExceptions._
import akka.http.scaladsl.server.Route

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * created by Kartik on 11-11-2017
  */
class VehicleCategoryService {
   self: VehicleCategoryFacadeComponent =>

   //save vehicle category
   def insertVehicleCategory(vehicleCategory: VehicleCategory): Future[String] = {
      for {
         vehicleCategoryList <- vehicleCategoryRepository.vehicleCategoryFuture
         res = if (vehicleCategory.name.nonEmpty) {
            if (vehicleCategoryList.nonEmpty) {
               val definedOption = vehicleCategoryList.find(_.name.toLowerCase == vehicleCategory.name.toLowerCase)
               if (definedOption.isEmpty) {
                  vehicleCategoryRepository.insert(vehicleCategory)
                  Future.successful("Inserted vehicle category successfully")
               } else throw DuplicateEntityException(exception = new Exception("Category Name already defined!!"), message = "Category Name already defined!!")
            } else {
               vehicleCategoryRepository.insert(vehicleCategory)
               Future.successful("Inserted vehicle category successfully")
            }
         } else throw FieldNotDefinedException(exception = new Exception("Name is not defined!!"), message = "Name is not defined!!")
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
            } else throw NoSuchEntityException(exception = new Exception("Vehicle category not found for given id!!"), message = "Vehicle category not found for given id!!")
         } else throw EmptyListException(exception = new Exception("Vehicle category list is empty"), message = "Vehicle category list is empty")
      } yield res
   }.recover { case ex => handleExceptions(ex) }

   // delete vehicle category by id
   def deleteVehicleCategoryById(id: Long): Future[String] = {
      for {
         vehicleCategoryList <- vehicleCategoryRepository.vehicleCategoryFuture
         vehicleTypeList <- vehicleTypeRepository.vehicleTypeFuture
         res = if (vehicleCategoryList.nonEmpty) {
            val vehicleCategoryOption = vehicleCategoryList.find(_.vehicleCategoryId == id)
            if (vehicleCategoryOption.isDefined) {
               val vehicleTypesForCategory = vehicleTypeList.filter(_.vehicleCategoryId == id)
               if (vehicleTypesForCategory.isEmpty) {
                  vehicleCategoryRepository.delete(id)
                  Future.successful("Deleted vehicle category successfully")
               } else throw ForeignKeyRelationFoundException(exception = new Exception("Foreign key relation found in vehicle type table!!"), message = "Foreign key relation found in vehicle type table!!")
            } else throw NoSuchEntityException(exception = new Exception("Vehicle category not found for given id!!"), message = "Vehicle category not found for given id!!")
         } else throw EmptyListException(exception = new Exception("Vehicle category list is empty!!"), message = "Vehicle category list is empty!!")
      } yield res
   }.flatten.recover { case ex => handleExceptions(ex) }

   // update vehicle category by id
   def updateVehicleCategoryById(id: Long, updatedVehicleCategory: VehicleCategory): Future[String] = {
      for {
         vehicleCategoryList <- vehicleCategoryRepository.vehicleCategoryFuture
         res = if (updatedVehicleCategory.name.nonEmpty) {
            if (vehicleCategoryList.nonEmpty) {
               val vehicleCategoryOption = vehicleCategoryList.find(_.vehicleCategoryId == id)
               if (vehicleCategoryOption.isDefined) {
                  vehicleCategoryRepository.update(id, updatedVehicleCategory)
                  Future.successful("Updated vehicle category successfully")
               } else throw NoSuchEntityException(exception = new Exception("Vehicle category not found for given id!!"), message = "Vehicle category not found for given id!!")
            } else throw EmptyListException(exception = new Exception("Vehicle category list is empty!!"), message = "Vehicle category list is empty!!")
         } else throw FieldNotDefinedException(exception = new Exception("Fields are not defined!!"), message = "Fields are not defined!!")
      } yield res
   }.flatten.recover { case ex => handleExceptions(ex) }

   def getAll: Future[Seq[VehicleCategory]] = {
      for {
         categoryList <- vehicleCategoryRepository.vehicleCategoryFuture
         res = if (categoryList.nonEmpty) {categoryList} else throw EmptyListException(message = "Vehicle category list is empty!!", exception = new Exception("Vehicle category list is empty!!"))
      } yield res
   }
}

object ImplVehicleCategoryService extends VehicleCategoryService with VehicleCategoryFacade

class VehicleCategoryRest(vehicleCategoryService: VehicleCategoryService) extends CustomDirectives {

   val vehicleCategoryRoute: Route = path("vehiclecategory") {
      pathEndOrSingleSlash {
         get {
            val result = vehicleCategoryService.getAll
            complete(respond(result))
         } ~ post {
            entity(as[String]) {
               category =>
                  val newCategory = read[VehicleCategory](category)
                  val result = vehicleCategoryService.insertVehicleCategory(newCategory)
                  complete(respond(result))
            }
         }
      }
   } ~ path("vehiclecategory" / LongNumber) {
      id =>
         get {
            val result = vehicleCategoryService.getVehicleCategoryById(id)
            complete(respond(result))
         } ~ put {
            entity(as[String]) {
               category =>
                  val updateCategory = read[VehicleCategory](category)
                  val result = vehicleCategoryService.updateVehicleCategoryById(id, updateCategory)
                  complete(respond(result))
            }
         } ~ delete {
            val result = vehicleCategoryService.deleteVehicleCategoryById(id)
            complete(respond(result))
         }
   }
}