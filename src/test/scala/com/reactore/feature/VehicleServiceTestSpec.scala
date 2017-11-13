package com.reactore.feature

import com.reactore.core._
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.Future

/**
  * created by Kartik on 13-11-2017
  */
class VehicleServiceTestSpec extends WordSpec with ScalaFutures with Matchers {
   "Vehicle Service" should {
      // test cases for getAll method
      "return all vehicle list " in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         val result = MockVehicleService.getAll
         val expectedResult = MockVehicleRepository.vehicleList
         result.futureValue shouldBe expectedResult
      }
      "throw exception in get all for empty vehicle list" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.emptyList)
         val result = MockVehicleService.getAll
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "get vehicle in get by id for vehicle id as 1" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         val expectedResult = MockVehicleRepository.vehicle1
         val result = MockVehicleService.getVehicleById(1)
         result.futureValue shouldBe expectedResult
      }
      "throw exception in get vehicle for vehicle id as 5" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         val result = MockVehicleService.getVehicleById(5)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in get vehicle for  empty vehicle list" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.emptyList)
         val result = MockVehicleService.getVehicleById(1)
         result.failed.futureValue shouldBe an[EmptyListException]
      }

      //test cases for insertVehicle method
      "insert a new vehicle " in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         when(MockVehicleService.vehicleRepository.insert(any[Vehicle])).thenReturn(Future.successful(1))
         val newVehicle = Vehicle(5, "Bike", modelNumber = "BIKE", vehicleType = 3, company = 1)
         val result = MockVehicleService.insertVehicle(newVehicle)
         result.futureValue shouldBe "Inserted vehicle successfully"
      }
      "throw exception in insert vehicle if fields are not defined" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val newVehicle = Vehicle(5, "", modelNumber = "", vehicleType = 3, company = 1)
         val result = MockVehicleService.insertVehicle(newVehicle)
         result.failed.futureValue shouldBe an[FieldNotDefinedException]
      }
      "throw exception in insert vehicle if company list is empty" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.emptyList)
         val newVehicle = Vehicle(5, "Bike", modelNumber = "BIKE", vehicleType = 3, company = 1)
         val result = MockVehicleService.insertVehicle(newVehicle)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in insert vehicle if vehicle type list is empty" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.emptyList)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val newVehicle = Vehicle(5, "Bike", modelNumber = "BIKE", vehicleType = 3, company = 1)
         val result = MockVehicleService.insertVehicle(newVehicle)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in insert vehicle if company does not exists" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val newVehicle = Vehicle(5, "Bike", modelNumber = "BIKE", vehicleType = 3, company = 4)
         val result = MockVehicleService.insertVehicle(newVehicle)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in insert vehicle if vehicle type does not exists" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val newVehicle = Vehicle(5, "Bike", modelNumber = "BIKE", vehicleType = 6, company = 2)
         val result = MockVehicleService.insertVehicle(newVehicle)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in insert vehicle if duplicate model number inserted" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val newVehicle = Vehicle(5, "Winger", modelNumber = "WINGER", vehicleType = 4, company = 2)
         val result = MockVehicleService.insertVehicle(newVehicle)
         result.failed.futureValue shouldBe an[UniqueKeyViolationException]
      }

      //test cases for deleteVehicleById method
      "delete vehicle in delete vehicle for id 1" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleRepository.delete(anyLong)).thenReturn(Future.successful(1))
         val result = MockVehicleService.deleteVehicleById(1)
         result.futureValue shouldBe "Deleted vehicle successfully"
      }
      "throw exception in delete vehicle if vehicle list is empty" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.emptyList)
         val result = MockVehicleService.deleteVehicleById(1)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in delete vehicle if vehicle id not found for id as 6" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         val result = MockVehicleService.deleteVehicleById(6)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }

      //test cases for updateVehicleById method
      "update vehicle details" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         when(MockVehicleService.vehicleRepository.update(anyLong, any[Vehicle])).thenReturn(Future.successful(1))
         val updatedVehicle = Vehicle(1, "Bike", modelNumber = "BIKE", vehicleType = 3, company = 3)
         val result = MockVehicleService.updateVehicleById(1, updatedVehicle)
         result.futureValue shouldBe "Updated vehicle successfully"
      }
      "throw exception in update vehicle if fields are not defined" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val updatedVehicle = Vehicle(1, "", modelNumber = "", vehicleType = 3, company = 3)
         val result = MockVehicleService.updateVehicleById(1, updatedVehicle)
         result.failed.futureValue shouldBe an[FieldNotDefinedException]
      }
      "throw exception in update vehicle if vehicle id does not exists for id as 5 " in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val updatedVehicle = Vehicle(5, "Bike", modelNumber = "BIKE", vehicleType = 3, company = 3)
         val result = MockVehicleService.updateVehicleById(5, updatedVehicle)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in update vehicle for company id as 5 " in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val updatedVehicle = Vehicle(1, "Bike", modelNumber = "BIKE", vehicleType = 3, company = 5)
         val result = MockVehicleService.updateVehicleById(1, updatedVehicle)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in update vehicle for vehicle type id as 6 " in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val updatedVehicle = Vehicle(1, "Bike", modelNumber = "BIKE", vehicleType = 6, company = 1)
         val result = MockVehicleService.updateVehicleById(1, updatedVehicle)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in update vehicle if vehicle has duplicate model number " in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val updatedVehicle = Vehicle(1, "TIAGO-R", modelNumber = "TIAGO", vehicleType = 1, company = 1)
         val result = MockVehicleService.updateVehicleById(1, updatedVehicle)
         result.failed.futureValue shouldBe an[UniqueKeyViolationException]
      }
   }
}

object MockVehicleService extends VehicleService with MockVehicleFacade