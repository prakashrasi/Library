package com.reactore.feature

/**
  * created by Kartik on 11-11-2017
  */

import java.sql.Timestamp

import akka.http.scaladsl.server.Route
import com.reactore.core.HandleExceptions._
import com.reactore.core._
import org.joda.time.{DateTime, Years}
import org.joda.time.format.DateTimeFormat
import org.json4s.native.Serialization._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class VehicleDetails(quantity: Long, weight: Long, description: Option[String])

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
         companyOption = companyList.find(_.companyId == vehicle.companyId)
         _ = if (companyOption.isEmpty) throw NoSuchEntityException(exception = new Exception("Company not found!!"), message = "Company not found!!")
         _ = if (vehicleTypeList.isEmpty) throw EmptyListException(exception = new Exception("Vehicle type list is empty!!"), message = "Vehicle type list is empty!!")
         vehicleTypeOption = vehicleTypeList.find(_.vehicleTypeId == vehicle.vehicleTypeId)
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

   // update vehicle by id
   def updateVehicleById(id: Long, updatedVehicle: Vehicle): Future[String] = {
      val result = for {
         vehicleList <- vehicleRepository.vehiclesFuture
         vehicleTypeList <- vehicleTypeRepository.vehicleTypeFuture
         companyList <- companyRepository.companyFuture
         _ = if (updatedVehicle.name.isEmpty && updatedVehicle.modelNumber.isEmpty) throw FieldNotDefinedException(exception = new Exception("All fields are not defined!!"), message = "All fields are not defined!!")
         vehicleOption = vehicleList.find(vehicle => vehicle.vehicleId == id)
         _ = if (vehicleOption.isEmpty) throw NoSuchEntityException(exception = new Exception("Vehicle not found for given id!!"), message = "Vehicle not found for given id!!")
         validCompany = companyList.find(_.companyId == updatedVehicle.companyId)
         _ = if (validCompany.isEmpty) throw NoSuchEntityException(exception = new Exception("Company not found for updated data!!"), message = "Company not found for updated data!!")
         validVehicleType = vehicleTypeList.find(_.vehicleTypeId == updatedVehicle.vehicleTypeId)
         _ = if (validVehicleType.isEmpty) throw NoSuchEntityException(exception = new Exception("Vehicle Type not found for updated data!!"), message = "Vehicle Type not found for updated data!!")
         uniqueVehicle = vehicleList.find(_.modelNumber.equalsIgnoreCase(updatedVehicle.modelNumber))
         _ = if (uniqueVehicle.isDefined) throw UniqueKeyViolationException(exception = new Exception("Updated vehicle has duplicate model number!!"), message = "Updated vehicle has duplicate model number!!")
         res <- vehicleRepository.update(id, updatedVehicle).map { x => "Updated vehicle successfully" }
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   // get all vehicles
   def getAll: Future[Seq[Vehicle]] = {
      val result = for {
         vehicleList <- vehicleRepository.vehiclesFuture
         _ = if (vehicleList.isEmpty) throw EmptyListException(message = "Vehicle list is empty!!", exception = new Exception("Vehicle list is empty!!"))
         res = vehicleList
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   // group vehicle by company
   def groupVehicleByCompany: Future[Seq[VehiclesByCompanyContainer]] = {
      val result = for {
         vehicleList <- vehicleRepository.vehiclesFuture
         companyList <- companyRepository.companyFuture
         _ = if (vehicleList.isEmpty) throw EmptyListException(message = "Vehicle list is empty", exception = new Exception("Vehicle list is empty"))
         _ = if (companyList.isEmpty) throw EmptyListException(message = "Company list is empty", exception = new Exception("Company list is empty"))
         res = vehicleList.groupBy(_.companyId).map {
            case (company, vehicles) =>
               val companyOption = companyList.find(_.companyId == company)
               val companyName = if (companyOption.isDefined) {companyOption.get.name} else throw NoSuchEntityException(message = "Company not found", exception = new Exception("Company not found"))
               VehiclesByCompanyContainer(companyName, vehicles.sortBy(_.vehicleId))
         }.toSeq.sortBy(_.companyName)
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   //get vehicles by category
   def getVehiclesByCategory(categoryId: Long): Future[Seq[Vehicle]] = {
      val result = for {
         vehicleList <- vehicleRepository.vehiclesFuture
         vehicleTypeList <- vehicleTypeRepository.vehicleTypeFuture
         _ = if (vehicleList.isEmpty) throw EmptyListException(message = "Vehicle list is empty", exception = new Exception("Vehicle list is empty"))
         _ = if (vehicleTypeList.isEmpty) throw EmptyListException(message = "Vehicle type list is empty", exception = new Exception("Vehicle type list is empty"))
         vehicleTypeIdList = vehicleTypeList.filter(_.vehicleCategoryId == categoryId).map(_.vehicleTypeId)
         _ = if (vehicleTypeIdList.isEmpty) throw NoSuchEntityException(message = "Vehicles not found for given category", exception = new Exception("Vehicles not found for given category"))
         vehiclesForType = vehicleList.filter(vehicle => vehicleTypeIdList.contains(vehicle.vehicleTypeId))
         res = if (vehiclesForType.nonEmpty) {vehiclesForType} else throw NoSuchEntityException(message = "Vehicles not found for given category", exception = new Exception("Vehicles not found for given category"))
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   //group vehicles by category
   def groupVehiclesByCategory: Future[Seq[VehiclesByCategoryContainer]] = {
      val result = for {
         vehicleList <- vehicleRepository.vehiclesFuture
         vehicleTypeList <- vehicleTypeRepository.vehicleTypeFuture
         vehicleCategoryList <- vehicleCategoryRepository.vehicleCategoryFuture
         _ = if (vehicleList.isEmpty) throw EmptyListException(message = "Vehicle list is empty", exception = new Exception("Vehicle list is empty"))
         _ = if (vehicleTypeList.isEmpty) throw EmptyListException(message = "Vehicle type list is empty", exception = new Exception("Vehicle type list is empty"))
         _ = if (vehicleCategoryList.isEmpty) throw EmptyListException(message = "Vehicle category list is empty", exception = new Exception("Vehicle category list is empty"))
         res = vehicleTypeList.groupBy(_.vehicleCategoryId).map {
            case (vehicleCategoryId, vehicleTypes) =>
               val categoryOption = vehicleCategoryList.find(_.vehicleCategoryId == vehicleCategoryId)
               val categoryName = if (categoryOption.isDefined) categoryOption.get.name else throw NoSuchEntityException(message = "No category found for given id", exception = new Exception("No category found for given id"))
               val vehicleTypeIdList = vehicleTypes.map(_.vehicleTypeId)
               val vehiclesForCategory = vehicleList.filter(vehicle => vehicleTypeIdList.contains(vehicle.vehicleTypeId))
               VehiclesByCategoryContainer(categoryName, vehiclesForCategory.sortBy(_.vehicleId))
         }.toSeq.sortBy(_.categoryName)
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   //get vehicles with capacity greater than specified
   def getVehiclesWithCapacityGreaterThan(capacity: Double): Future[Seq[Vehicle]] = {
      val result = for {
         vehicleList <- vehicleRepository.vehiclesFuture
         vehicleTypeList <- vehicleTypeRepository.vehicleTypeFuture
         vehicleCategoryList <- vehicleCategoryRepository.vehicleCategoryFuture
         _ = if (vehicleList.isEmpty) throw EmptyListException(message = "Vehicle list is empty", exception = new Exception("Vehicle list is empty"))
         _ = if (vehicleTypeList.isEmpty) throw EmptyListException(message = "Vehicle type list is empty", exception = new Exception("Vehicle type list is empty"))
         _ = if (vehicleCategoryList.isEmpty) throw EmptyListException(message = "Vehicle category list is empty", exception = new Exception("Vehicle category list is empty"))
         categoriesWithMaxCapacity = vehicleCategoryList.filter(_.maxCapacity > capacity).map(_.vehicleCategoryId)
         _ = if (categoriesWithMaxCapacity.isEmpty) throw NoSuchEntityException(message = "No categories found", exception = new Exception("No categories found"))
         vehicleTypesWithMaxCapacity = vehicleTypeList.filter(vehicleType => categoriesWithMaxCapacity.contains(vehicleType.vehicleCategoryId)).map(_.vehicleTypeId)
         _ = if (vehicleTypesWithMaxCapacity.isEmpty) throw NoSuchEntityException(message = "No vehicle types found", exception = new Exception("No vehicle types found"))
         vehiclesWithMaxCapacity = vehicleList.filter(vehicle => vehicleTypesWithMaxCapacity.contains(vehicle.vehicleTypeId))
         _ = if (vehiclesWithMaxCapacity.isEmpty) throw NoSuchEntityException(message = "No vehicles found", exception = new Exception("No vehicles found"))
         res = vehiclesWithMaxCapacity.sortBy(_.vehicleId)
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   //get number of vehicle by country
   def getVehicleCountByCountry(countryId: Long): Future[Int] = {
      val result = for {
         vehicleList <- vehicleRepository.vehiclesFuture
         companyList <- companyRepository.companyFuture
         _ = if (vehicleList.isEmpty) throw EmptyListException(message = "Vehicle list is empty", exception = new Exception("Vehicle list is empty"))
         _ = if (companyList.isEmpty) throw EmptyListException(message = "Company list is empty", exception = new Exception("Company list is empty"))
         companiesForGivenCountry = companyList.filter(_.countryId == countryId).map(_.companyId)
         _ = if (companiesForGivenCountry.isEmpty) throw NoSuchEntityException(message = "No companies found", exception = new Exception("No companies found"))
         vehiclesForGivenCountry = vehicleList.filter(vehicle => companiesForGivenCountry.contains(vehicle.companyId))
         _ = if (vehiclesForGivenCountry.isEmpty) throw NoSuchEntityException(message = "No vehicles found", exception = new Exception("No vehicles found"))
         res = vehiclesForGivenCountry.size
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   // update quantity, weight and description
   def updateVehicleDetails(id: Long, vehicleDetails: VehicleDetails): Future[String] = {
      val result = for {
         vehicleList <- vehicleRepository.vehiclesFuture
         _ = if (vehicleList.isEmpty) throw EmptyListException(message = "Vehicle list is empty", exception = new Exception("Vehicle list is empty"))
         vehicleOption = vehicleList.find(_.vehicleId == id)
         _ = if (vehicleOption.isEmpty) throw NoSuchEntityException(message = "No vehicle found for given id", exception = new Exception("No vehicle found for given id"))
         updatedVehicle = vehicleOption.get.copy(description = vehicleDetails.description, weight = vehicleDetails.weight, quantity = vehicleDetails.quantity)
         res <- vehicleRepository.update(id, updatedVehicle).map(x => "Updated vehicle details successfully")
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   //get vehicles by company older than given years
   def getVehiclesByCompanyOlderThan(years: Long): Future[Seq[Vehicle]] = {
      val currentTimeStamp = new Timestamp(System.currentTimeMillis())
      val currentTime = DateTime.now()
      val result = for {
         vehicleList <- vehicleRepository.vehiclesFuture
         companyList <- companyRepository.companyFuture
         _ = if (vehicleList.isEmpty) throw EmptyListException(message = "Vehicle list is empty", exception = new Exception("Vehicle list is empty"))
         _ = if (companyList.isEmpty) throw EmptyListException(exception = new Exception("Company list is empty!!"), message = "Company list is empty!!")
         companyIdList = companyList.filter { company =>
            (currentTimeStamp.getYear - company.startYear.getYear) > years
            //val startYear = DateTime.parse(company.startYear.toString, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.sss"))
            //Years.yearsBetween(startYear, currentTime).getYears > years
         }.map(_.companyId)
         _ = if (companyIdList.isEmpty) throw NoSuchEntityException(message = "No Companies found", exception = new Exception("No Companies found"))
         vehiclesForCompany = vehicleList.filter(vehicle => companyIdList.contains(vehicle.companyId))
         res = if (vehiclesForCompany.nonEmpty) {vehiclesForCompany.sortBy(_.vehicleId)} else throw NoSuchEntityException(message = "No Vehicles found", exception = new Exception("No Vehicles found"))
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
   } ~ path("vehicle" / "category") {
      get {
         val result = vehicleService.groupVehiclesByCategory
         complete(respond(result))
      }
   } ~ path("vehicle" / "category" / LongNumber) {
      id =>
         get {
            val result = vehicleService.getVehiclesByCategory(id)
            complete(respond(result))
         }
   } ~ path("vehicle" / "company") {
      pathEndOrSingleSlash {
         get {
            val result = vehicleService.groupVehicleByCompany
            complete(respond(result))
         }
      }
   } ~ path("vehicle" / "capacity" / LongNumber) {
      capacity =>
         get {
            val result = vehicleService.getVehiclesWithCapacityGreaterThan(capacity)
            complete(respond(result))
         }
   } ~ path("vehicle" / "countbycountry" / LongNumber) { id =>
      get {
         val result = vehicleService.getVehicleCountByCountry(id)
         complete(respond(result))
      }
   } ~ path("vehicle" / "company" / LongNumber) {
      years =>
         get {
            val result = vehicleService.getVehiclesByCompanyOlderThan(years)
            complete(respond(result))
         }
   } ~ path("vehicle" / "details" / LongNumber) {
      id =>
         put {
            entity(as[String]) {
               details =>
                  val updatedDetails = read[VehicleDetails](details)
                  val result = vehicleService.updateVehicleDetails(id, updatedDetails)
                  complete(respond(result))
            }
         }
   }

}



