package com.reactore.feature

import akka.http.scaladsl.server.Route
import com.reactore.core.HandleExceptions._
import com.reactore.core._
import org.json4s.native.Serialization._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * created by Kartik on 11-11-2017
  */
class VehicleService {
   self: VehicleFacadeComponent =>

   //save vehicle
   def insertVehicle(vehicle: Vehicle): Future[String] = {
      val result = for {
         vehicleList <- vehicleRepository.vehiclesFuture
         vehicleTypeList <- vehicleTypeRepository.vehicleTypeFuture
         companyList <- companyRepository.companyFuture
         _ = if (vehicle.name.isEmpty && vehicle.modelNumber.isEmpty) throw FieldNotDefinedException(exception = new Exception("Fields are not defined!!"), message = "Fields are not defined!!")
         _ = if (companyList.isEmpty) throw EmptyListException(exception = new Exception("Company list is empty!!"), message = "Company list is empty!!")
         companyOption = companyList.find(_.companyId == vehicle.company)
         _ = if (companyOption.isEmpty) throw NoSuchEntityException(exception = new Exception("Company not found!!"), message = "Company not found!!")
         _ = if (vehicleTypeList.isEmpty) throw EmptyListException(exception = new Exception("Vehicle type list is empty!!"), message = "Vehicle type list is empty!!")
         vehicleTypeOption = vehicleTypeList.find(_.vehicleTypeId == vehicle.vehicleType)
         _ = if (vehicleTypeOption.isEmpty) throw NoSuchEntityException(exception = new Exception("Vehicle type not found!!"), message = "Vehicle type not found!!")
         res <- if (vehicleList.nonEmpty) {
            val uniqueModelNumber = vehicleList.find(_.modelNumber.equalsIgnoreCase(vehicle.modelNumber))
            if (uniqueModelNumber.isEmpty) {
               vehicleRepository.insert(vehicle).map { x => "Inserted vehicle successfully" }
            } else Future.failed(UniqueKeyViolationException(exception = new Exception("Unique model number violated!!"), message = "Unique model number violated!!"))
         } else vehicleRepository.insert(vehicle).map { x => "Inserted vehicle successfully" }
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   //get vehicle by id
   def getVehicleById(id: Long): Future[Vehicle] = {
      val result = for {
         vehicleList <- vehicleRepository.vehiclesFuture
         _ = if (vehicleList.isEmpty) throw EmptyListException(exception = new Exception("Vehicle list is empty!!"), message = "Vehicle list is empty!!")
         vehicleOption = vehicleList.find(_.vehicleId == id)
         _ = if (vehicleOption.isEmpty) throw NoSuchEntityException(exception = new Exception("Vehicle not found for given id !!"), message = "Vehicle not found for given id !!")
         res = vehicleOption.get
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   //delete vehicle by id
   def deleteVehicleById(id: Long): Future[String] = {
      val result = for {
         vehicleList <- vehicleRepository.vehiclesFuture
         _ = if (vehicleList.isEmpty) throw EmptyListException(exception = new Exception("Vehicle list is empty!!"), message = "Vehicle list is empty!!")
         vehicleOption = vehicleList.find(_.vehicleId == id)
         _ = if (vehicleOption.isEmpty) throw NoSuchEntityException(exception = new Exception("Vehicle not found for given id !!"), message = "Vehicle not found for given id !!")
         res <- vehicleRepository.delete(id).map { x => "Deleted vehicle successfully" }
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   def updateVehicleById(id: Long, updatedVehicle: Vehicle): Future[String] = {
      val result = for {
         vehicleList <- vehicleRepository.vehiclesFuture
         vehicleTypeList <- vehicleTypeRepository.vehicleTypeFuture
         companyList <- companyRepository.companyFuture
         _ = if (updatedVehicle.name.isEmpty && updatedVehicle.modelNumber.isEmpty) throw FieldNotDefinedException(exception = new Exception("All fields are not defined!!"),message = "All fields are not defined!!")
         vehicleOption = vehicleList.find(vehicle => vehicle.vehicleId == id)
         _ = if (vehicleOption.isEmpty) throw NoSuchEntityException(exception = new Exception("Vehicle not found for given id!!"), message = "Vehicle not found for given id!!")
         validCompany = companyList.find(_.companyId == updatedVehicle.company)
         _ = if (validCompany.isEmpty) throw NoSuchEntityException(exception = new Exception("Company not found for updated data!!"), message = "Company not found for updated data!!")
         validVehicleType = vehicleTypeList.find(_.vehicleTypeId == updatedVehicle.vehicleType)
         _ = if (validVehicleType.isEmpty) throw NoSuchEntityException(exception = new Exception("Vehicle Type not found for updated data!!"), message = "Vehicle Type not found for updated data!!")
         uniqueVehicle = vehicleList.find(_.modelNumber.equalsIgnoreCase(updatedVehicle.modelNumber))
         _ = if (uniqueVehicle.isDefined) throw UniqueKeyViolationException(exception = new Exception("Updated vehicle has duplicate model number!!"), message = "Updated vehicle has duplicate model number!!")
         res <- vehicleRepository.update(id, updatedVehicle).map { x => "Updated vehicle successfully" }
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   def getAll: Future[Seq[Vehicle]] = {
      val result = for {
         vehicleList <- vehicleRepository.vehiclesFuture
         _ = if (vehicleList.isEmpty) throw EmptyListException(message = "Vehicle list is empty!!", exception = new Exception("Vehicle list is empty!!"))
         res = vehicleList
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }
}


object ImplVehicleService extends VehicleService with VehicleFacade

class VehicleRest(vehicleService: VehicleService) extends CustomDirectives {
   val vehicleRoute: Route = path("vehicle") {
      pathEndOrSingleSlash {
         get {
            val result = vehicleService.getAll
            complete(respond(result))
         } ~ post {
            entity(as[String]) { vehicle =>
               val newVehicle = read[Vehicle](vehicle)
               val result = vehicleService.insertVehicle(newVehicle)
               complete(respond(result))
            }
         }
      }
   } ~ path("vehicle" / LongNumber) { id =>
      get {
         val result = vehicleService.getVehicleById(id)
         complete(respond(result))
      } ~ put {
         entity(as[String]) {
            vehicle =>
               val updatedVehicle = read[Vehicle](vehicle)
               val result = vehicleService.updateVehicleById(id, updatedVehicle)
               complete(respond(result))
         }
      } ~ delete {
         val result = vehicleService.deleteVehicleById(id)
         complete(respond(result))
      }
   }
}

