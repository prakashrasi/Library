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
class VehicleTypeService {
   self: VehicleTypeFacadeComponent =>

   // save vehicle type
   def insertVehicleType(vehicleType: VehicleType): Future[Int] = {
      for {
         vehicleCategoryList <- vehicleCategoryRepository.vehicleCategoryFuture
         vehicleTypeList <- vehicleTypeRepository.vehicleTypeFuture
         res = if (vehicleType.name.nonEmpty) {
            if (vehicleCategoryList.nonEmpty) {
               val categoryOption = vehicleCategoryList.find(_.vehicleCategoryId == vehicleType.vehicleCategoryId)
               if (categoryOption.isDefined) {
                  if (vehicleTypeList.nonEmpty) {
                     val validTypeOption = vehicleTypeList.find(_.name.equalsIgnoreCase(vehicleType.name))
                     if (validTypeOption.isEmpty) {
                        vehicleTypeRepository.insert(vehicleType)
                     } else throw DuplicateEntityException(exception = new Exception("Vehicle Type already defined!!"))
                  } else vehicleTypeRepository.insert(vehicleType)
               } else throw NoSuchEntityException(exception = new Exception("Category does not exists!!"))
            } else throw EmptyListException(exception = new Exception("Category list is empty!!"))
         } else throw FieldNotDefinedException(exception = new Exception("Name is not defined!!"))
      } yield res
   }.flatten.recover { case ex => handleExceptions(ex) }

   // get vehicle type by id
   def getVehicleTypeById(id: Long): Future[VehicleType] = {
      for {
         vehicleTypeList <- vehicleTypeRepository.vehicleTypeFuture
         res = if (vehicleTypeList.nonEmpty) {
            val vehicleTypeOption = vehicleTypeList.find(_.vehicleTypeId == id)
            if (vehicleTypeOption.isDefined) {
               vehicleTypeOption.get
            } else throw NoSuchEntityException(exception = new Exception("Vehicle type not found for given id!!"))
         } else throw EmptyListException(exception = new Exception("Vehicle type list is empty!!"))
      } yield res
   }.recover { case ex => handleExceptions(ex) }

   // delete vehicle type by id
   def deleteVehicleTypeById(id: Long): Future[Int] = {
      for {
         vehicleList <- vehicleRepository.vehiclesFuture
         vehicleTypeList <- vehicleTypeRepository.vehicleTypeFuture
         res = if (vehicleTypeList.nonEmpty) {
            val vehicleTypeOption = vehicleTypeList.find(_.vehicleTypeId == id)
            if (vehicleTypeOption.isDefined) {
               val vehicleForGivenType = vehicleList.filter(_.vehicleType == id)
               if (vehicleForGivenType.isEmpty) {
                  vehicleTypeRepository.delete(id)
               } else throw ForeignKeyRelationFoundException(exception = new Exception("Foreign key relation found in vehicle table!!"))
            } else throw NoSuchEntityException(exception = new Exception("Vehicle type not found for given id!!"))
         } else throw EmptyListException(exception = new Exception("Vehicle type list is empty!!"))
      } yield res
   }
     .flatten.recover { case ex => handleExceptions(ex) }

   // update vehicle type by id
   def updateVehicleTypeById(id: Long, updatedVehicleType: VehicleType): Future[Int] = {
      for {
         vehicleTypeList <- vehicleTypeRepository.vehicleTypeFuture
         vehicleCategoryList <- vehicleCategoryRepository.vehicleCategoryFuture
         res = if (updatedVehicleType.name.nonEmpty) {
            if (vehicleTypeList.nonEmpty) {
               val vehicleTypeOption = vehicleTypeList.find(_.vehicleTypeId == id)
               if (vehicleTypeOption.isDefined) {
                  val validVehicleCategory = vehicleCategoryList.find(_.vehicleCategoryId == updatedVehicleType.vehicleCategoryId)
                  if (validVehicleCategory.isDefined) {
                     vehicleTypeRepository.update(id, updatedVehicleType)
                  } else throw NoSuchEntityException(exception = new Exception("Vehicle Category not found for updated vehicle type!!"))
               } else throw NoSuchEntityException(exception = new Exception("Vehicle type not found given id!!"))
            } else throw EmptyListException(exception = new Exception("Vehicle type list is empty!!"))
         } else throw FieldNotDefinedException(exception = new Exception("Fields are not defined!!"))
      } yield res
   }.flatten.recover { case ex => handleExceptions(ex) }

}

object ImplVehicleTypeService extends VehicleTypeService with VehicleTypeFacade

class VehicleTypeRest(vehicleTypeService: VehicleTypeService) extends CustomDirectives {
   val typeServiceObj = new VehicleTypeService with VehicleTypeFacade

   val vehicleTypeRoute: Route = path("vehiclecategory") {
      pathEndOrSingleSlash {
         get {
            val result = typeServiceObj.vehicleTypeRepository.vehicleTypeFuture
            complete(respond(result))
         } ~ put {
            entity(as[String]) {
               vehiclType =>
                  val newType = read[VehicleType](vehiclType)
                  val result = typeServiceObj.insertVehicleType(newType)
                  complete(respond(result))
            }
         }
      }
   } ~ path("vehiclecategory" / LongNumber) {
      id =>
         get {
            val result = typeServiceObj.getVehicleTypeById(id)
            complete(respond(result))
         } ~ post {
            entity(as[String]) {
               vehicleType =>
                  val updateType = read[VehicleType](vehicleType)
                  val result = typeServiceObj.updateVehicleTypeById(id, updateType)
                  complete(respond(result))
            }
         } ~ delete {
            val result = typeServiceObj.deleteVehicleTypeById(id)
            complete(respond(result))
         }
   }
}
