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
                  vehicleCategoryRepository.update(id, updatedVehicleCategory)
               } else throw NoSuchEntityException(exception = new Exception("Vehicle category not found for given id!!"))
            } else throw EmptyListException(exception = new Exception("Vehicle category list is empty!!"))
         } else throw FieldNotDefinedException(exception = new Exception("Fields are not defined!!"))
      } yield res
   }.flatten.recover { case ex => handleExceptions(ex) }
}

object ImplVehicleCategoryService extends VehicleCategoryService with VehicleCategoryFacade

class VehicleCategoryRest(vehicleCategoryService: VehicleCategoryService) extends CustomDirectives {

   val categoryServiceObj = new VehicleCategoryService with VehicleCategoryFacade

   val vehicleCategoryRoute: Route = path("vehiclecategory") {
      pathEndOrSingleSlash {
         get {
            val result = categoryServiceObj.vehicleCategoryRepository.vehicleCategoryFuture
            complete(respond(result))
         } ~ post {
            entity(as[String]) {
               category =>
                  val newCategory = read[VehicleCategory](category)
                  val result = categoryServiceObj.insertVehicleCategory(newCategory)
                  complete(respond(result))
            }
         }
      }
   } ~ path("vehiclecategory" / LongNumber) {
      id =>
         get {
            val result = categoryServiceObj.getVehicleCategoryById(id)
            complete(respond(result))
         } ~ put {
            entity(as[String]) {
               category =>
                  val updateCategory = read[VehicleCategory](category)
                  val result = categoryServiceObj.updateVehicleCategoryById(id, updateCategory)
                  complete(respond(result))
            }
         } ~ delete {
            val result = categoryServiceObj.deleteVehicleCategoryById(id)
            complete(respond(result))
         }
   }
}