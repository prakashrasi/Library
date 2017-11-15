package com.reactore.feature

/**
  * created by Kartik on 11-11-2017
  */

import org.json4s.native.Serialization._
import com.reactore.core._
import HandleExceptions._
import akka.http.scaladsl.server.Route

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class VehicleCategoryService {
   self: VehicleCategoryFacadeComponent =>

   //save vehicle category
   def insertVehicleCategory(vehicleCategory: VehicleCategory): Future[String] = {
      val result = for {
         vehicleCategoryList <- vehicleCategoryRepository.getAllCategory
         _ = if (vehicleCategory.name.isEmpty) throw FieldNotDefinedException(exception = new Exception("Name is not defined!!"), message = "Name is not defined!!")
         _ = if (vehicleCategoryList.isEmpty) vehicleCategoryRepository.insert(vehicleCategory).map { x => "Inserted vehicle category successfully" }
         definedOption = vehicleCategoryList.find(_.name.toLowerCase == vehicleCategory.name.toLowerCase)
         res <- if (definedOption.isEmpty) {
            vehicleCategoryRepository.insert(vehicleCategory).map { x => "Inserted vehicle category successfully" }
         } else Future.failed(DuplicateEntityException(exception = new Exception("Category Name already defined!!"), message = "Category Name already defined!!"))
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   // get vehicle category by id
   def getVehicleCategoryById(id: Long): Future[VehicleCategory] = {
      val result = for {
         vehicleCategoryList <- vehicleCategoryRepository.getAllCategory
         _ = if (vehicleCategoryList.isEmpty) throw EmptyListException(exception = new Exception("Vehicle category list is empty"), message = "Vehicle category list is empty")
         vehicleCategoryOption = vehicleCategoryList.find(_.vehicleCategoryId == id)
         res = if (vehicleCategoryOption.isDefined) {
            vehicleCategoryOption.get
         } else throw NoSuchEntityException(exception = new Exception("Vehicle category not found for given id!!"), message = "Vehicle category not found for given id!!")
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   // delete vehicle category by id
   def deleteVehicleCategoryById(id: Long): Future[String] = {
      val result = for {
         vehicleCategoryList <- vehicleCategoryRepository.getAllCategory
         vehicleTypeList <- vehicleTypeRepository.getAllVehicleTypes
         _ = if (vehicleCategoryList.isEmpty) throw EmptyListException(exception = new Exception("Vehicle category list is empty!!"), message = "Vehicle category list is empty!!")
         vehicleCategoryOption = vehicleCategoryList.find(_.vehicleCategoryId == id)
         _ = if (vehicleCategoryOption.isEmpty) throw NoSuchEntityException(exception = new Exception("Vehicle category not found for given id!!"), message = "Vehicle category not found for given id!!")
         vehicleTypesForCategory = vehicleTypeList.filter(_.vehicleCategoryId == id)
         res <- if (vehicleTypesForCategory.isEmpty) {
            vehicleCategoryRepository.delete(id).map { x => "Deleted vehicle category successfully" }
         } else Future.failed(ForeignKeyRelationFoundException(exception = new Exception("Foreign key relation found in vehicle type table!!"), message = "Foreign key relation found in vehicle type table!!"))
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   // update vehicle category by id
   def updateVehicleCategoryById(id: Long, updatedVehicleCategory: VehicleCategory): Future[String] = {
      val result = for {
         vehicleCategoryList <- vehicleCategoryRepository.getAllCategory
         _ = if (updatedVehicleCategory.name.isEmpty) throw FieldNotDefinedException(exception = new Exception("Fields are not defined!!"), message = "Fields are not defined!!")
         _ = if (vehicleCategoryList.isEmpty) throw EmptyListException(exception = new Exception("Vehicle category list is empty!!"), message = "Vehicle category list is empty!!")
         vehicleCategoryOption = vehicleCategoryList.find(_.vehicleCategoryId == id)
         res <- if (vehicleCategoryOption.isDefined) {
            vehicleCategoryRepository.update(id, updatedVehicleCategory).map { x => "Updated vehicle category successfully" }
         } else Future.failed(NoSuchEntityException(exception = new Exception("Vehicle category not found for given id!!"), message = "Vehicle category not found for given id!!"))
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   def getAll: Future[Seq[VehicleCategory]] = {
      val result = for {
         categoryList <- vehicleCategoryRepository.getAllCategory
         _ = if (categoryList.isEmpty) throw EmptyListException(message = "Vehicle category list is empty!!", exception = new Exception("Vehicle category list is empty!!"))
         res = categoryList
      } yield res
      result.recover { case ex => handleExceptions(ex) }
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