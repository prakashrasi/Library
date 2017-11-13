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
class VehicleTypeService {
   self: VehicleTypeFacadeComponent =>

   // save vehicle type
   def insertVehicleType(vehicleType: VehicleType): Future[String] = {
      val result = for {
         vehicleCategoryList <- vehicleCategoryRepository.vehicleCategoryFuture
         vehicleTypeList <- vehicleTypeRepository.vehicleTypeFuture
         _ = if (vehicleType.name.isEmpty) throw FieldNotDefinedException(exception = new Exception("Name is not defined!!"), message = "Name is not defined!!")
         _ = if (vehicleCategoryList.isEmpty) throw EmptyListException(exception = new Exception("Category list is empty!!"), message = "Category list is empty!!")
         categoryOption = vehicleCategoryList.find(_.vehicleCategoryId == vehicleType.vehicleCategoryId)
         res <- if (categoryOption.isDefined) {
            if (vehicleTypeList.nonEmpty) {
               val validTypeOption = vehicleTypeList.find(_.name.equalsIgnoreCase(vehicleType.name))
               if (validTypeOption.isEmpty) {
                  vehicleTypeRepository.insert(vehicleType).map { x => "Inserted vehicle type successfully" }
               } else Future.failed(DuplicateEntityException(exception = new Exception("Vehicle Type already defined!!"), message = "Vehicle Type already defined!!"))
            } else vehicleTypeRepository.insert(vehicleType).map(x => "Inserted vehicle type successfully")
         } else Future.failed(NoSuchEntityException(exception = new Exception("Category does not exists!!"), message = "Category does not exists!!"))
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   // get vehicle type by id
   def getVehicleTypeById(id: Long): Future[VehicleType] = {
      val result = for {
         vehicleTypeList <- vehicleTypeRepository.vehicleTypeFuture
         _ = if (vehicleTypeList.isEmpty) throw EmptyListException(exception = new Exception("Vehicle type list is empty!!"), message = "Vehicle type list is empty!!")
         vehicleTypeOption = vehicleTypeList.find(_.vehicleTypeId == id)
         res = if (vehicleTypeOption.isDefined) {
            vehicleTypeOption.get
         } else throw NoSuchEntityException(exception = new Exception("Vehicle type not found for given id!!"), message = "Vehicle type not found for given id!!")
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }


   // delete vehicle type by id
   def deleteVehicleTypeById(id: Long): Future[String] = {
      val result = for {
         vehicleList <- vehicleRepository.vehiclesFuture
         vehicleTypeList <- vehicleTypeRepository.vehicleTypeFuture
         _ = if (vehicleTypeList.isEmpty) throw EmptyListException(exception = new Exception("Vehicle type list is empty!!"), message = "Vehicle type list is empty!!")
         vehicleTypeOption = vehicleTypeList.find(_.vehicleTypeId == id)
         _ = if (vehicleTypeOption.isEmpty) throw NoSuchEntityException(exception = new Exception("Vehicle type not found for given id!!"), message = "Vehicle type not found for given id!!")
         res <- {
            val vehicleForGivenType = vehicleList.filter(_.vehicleType == id)
            if (vehicleForGivenType.isEmpty) {
               vehicleTypeRepository.delete(id).map { x => "Deleted vehicle type successfully" }
            } else Future.failed(ForeignKeyRelationFoundException(exception = new Exception("Foreign key relation found in vehicle table!!"), message = "Foreign key relation found in vehicle table!!"))
         }
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }


   // update vehicle type by id
   def updateVehicleTypeById(id: Long, updatedVehicleType: VehicleType): Future[String] = {
      val result = for {
         vehicleTypeList <- vehicleTypeRepository.vehicleTypeFuture
         vehicleCategoryList <- vehicleCategoryRepository.vehicleCategoryFuture
         _ = if (updatedVehicleType.name.isEmpty) throw FieldNotDefinedException(exception = new Exception("Fields are not defined!!"), message = "Fields are not defined!!")
         _ = if (vehicleTypeList.isEmpty) throw EmptyListException(exception = new Exception("Vehicle type list is empty!!"), message = "Vehicle type list is empty!!")
         vehicleTypeOption = vehicleTypeList.find(_.vehicleTypeId == id)
         _ = if (vehicleTypeOption.isEmpty) throw NoSuchEntityException(exception = new Exception("Vehicle type not found given id!!"), message = "Vehicle type not found given id!!")
         validVehicleCategory = vehicleCategoryList.find(_.vehicleCategoryId == updatedVehicleType.vehicleCategoryId)
         _ = if (validVehicleCategory.isEmpty) throw NoSuchEntityException(exception = new Exception("Vehicle Category not found for updated vehicle type!!"), message = "Vehicle Category not found for updated vehicle type!!")
         res <- vehicleTypeRepository.update(id, updatedVehicleType).map { x => "Updated vehicle type successfully" }

      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   def getAll: Future[Seq[VehicleType]] = {
      val result = for {
         vehicleTypeList <- vehicleTypeRepository.vehicleTypeFuture
         _ = if (vehicleTypeList.isEmpty) throw EmptyListException(message = "Vehicle type list is empty!!", exception = new Exception("Vehicle type list is empty!!"))
         res = vehicleTypeList
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }
}

object ImplVehicleTypeService extends VehicleTypeService with VehicleTypeFacade

class VehicleTypeRest(vehicleTypeService: VehicleTypeService) extends CustomDirectives {
   val vehicleTypeRoute: Route = path("vehicletype") {
      pathEndOrSingleSlash {
         get {
            val result = vehicleTypeService.getAll
            complete(respond(result))
         } ~ post {
            entity(as[String]) {
               vehiclType =>
                  val newType = read[VehicleType](vehiclType)
                  val result = vehicleTypeService.insertVehicleType(newType)
                  complete(respond(result))
            }
         }
      }
   } ~ path("vehicletype" / LongNumber) {
      id =>
         get {
            val result = vehicleTypeService.getVehicleTypeById(id)
            complete(respond(result))
         } ~ put {
            entity(as[String]) {
               vehicleType =>
                  val updateType = read[VehicleType](vehicleType)
                  val result = vehicleTypeService.updateVehicleTypeById(id, updateType)
                  complete(respond(result))
            }
         } ~ delete {
            val result = vehicleTypeService.deleteVehicleTypeById(id)
            complete(respond(result))
         }
   }
}
