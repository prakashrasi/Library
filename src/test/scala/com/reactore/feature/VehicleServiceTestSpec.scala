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
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         val result = MockVehicleService.getAll
         val expectedResult = MockVehicleRepository.vehicleList
         result.futureValue shouldBe expectedResult
      }
      "throw exception in get all for empty vehicle list" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.emptyList)
         val result = MockVehicleService.getAll
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "get vehicle in get by id for vehicle id as 1" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         val expectedResult = MockVehicleRepository.vehicle1
         val result = MockVehicleService.getVehicleById(1)
         result.futureValue shouldBe expectedResult
      }
      "throw exception in get vehicle for vehicle id as 5" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         val result = MockVehicleService.getVehicleById(5)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in get vehicle for  empty vehicle list" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.emptyList)
         val result = MockVehicleService.getVehicleById(1)
         result.failed.futureValue shouldBe an[EmptyListException]
      }

      //test cases for insertVehicle method
      "insert a new vehicle " in {
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         when(MockVehicleService.vehicleRepository.insert(any[Vehicle])).thenReturn(Future.successful(1))
         val newVehicle = Vehicle(5, "Bike", modelNumber = "BIKE", vehicleTypeId = 3, companyId = 1)
         val result = MockVehicleService.insertVehicle(newVehicle)
         result.futureValue shouldBe "Inserted vehicle successfully"
      }
      "throw exception in insert vehicle if fields are not defined" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         val newVehicle = Vehicle(5, "", modelNumber = "", vehicleTypeId = 3, companyId = 1)
         val result = MockVehicleService.insertVehicle(newVehicle)
         result.failed.futureValue shouldBe an[FieldNotDefinedException]
      }
      "throw exception in insert vehicle if company list is empty" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.emptyList)
         val newVehicle = Vehicle(5, "Bike", modelNumber = "BIKE", vehicleTypeId = 3, companyId = 1)
         val result = MockVehicleService.insertVehicle(newVehicle)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in insert vehicle if vehicle type list is empty" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.emptyList)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         val newVehicle = Vehicle(5, "Bike", modelNumber = "BIKE", vehicleTypeId = 3, companyId = 1)
         val result = MockVehicleService.insertVehicle(newVehicle)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in insert vehicle if company does not exists" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         val newVehicle = Vehicle(5, "Bike", modelNumber = "BIKE", vehicleTypeId = 3, companyId = 4)
         val result = MockVehicleService.insertVehicle(newVehicle)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in insert vehicle if vehicle type does not exists" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         val newVehicle = Vehicle(5, "Bike", modelNumber = "BIKE", vehicleTypeId = 6, companyId = 2)
         val result = MockVehicleService.insertVehicle(newVehicle)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in insert vehicle if duplicate model number inserted" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         val newVehicle = Vehicle(5, "Winger", modelNumber = "WINGER", vehicleTypeId = 4, companyId = 2)
         val result = MockVehicleService.insertVehicle(newVehicle)
         result.failed.futureValue shouldBe an[UniqueKeyViolationException]
      }

      //test cases for deleteVehicleById method
      "delete vehicle in delete vehicle for id 1" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleRepository.delete(anyLong)).thenReturn(Future.successful(1))
         val result = MockVehicleService.deleteVehicleById(1)
         result.futureValue shouldBe "Deleted vehicle successfully"
      }
      "throw exception in delete vehicle if vehicle list is empty" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.emptyList)
         val result = MockVehicleService.deleteVehicleById(1)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in delete vehicle if vehicle id not found for id as 6" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         val result = MockVehicleService.deleteVehicleById(6)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }

      //test cases for updateVehicleById method
      "update vehicle details" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         when(MockVehicleService.vehicleRepository.update(anyLong, any[Vehicle])).thenReturn(Future.successful(1))
         val updatedVehicle = Vehicle(1, "Bike", modelNumber = "BIKE", vehicleTypeId = 3, companyId = 3)
         val result = MockVehicleService.updateVehicleById(1, updatedVehicle)
         result.futureValue shouldBe "Updated vehicle successfully"
      }
      "throw exception in update vehicle if fields are not defined" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         val updatedVehicle = Vehicle(1, "", modelNumber = "", vehicleTypeId = 3, companyId = 3)
         val result = MockVehicleService.updateVehicleById(1, updatedVehicle)
         result.failed.futureValue shouldBe an[FieldNotDefinedException]
      }
      "throw exception in update vehicle if vehicle id does not exists for id as 5 " in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         val updatedVehicle = Vehicle(5, "Bike", modelNumber = "BIKE", vehicleTypeId = 3, companyId = 3)
         val result = MockVehicleService.updateVehicleById(5, updatedVehicle)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in update vehicle for company id as 5 " in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         val updatedVehicle = Vehicle(1, "Bike", modelNumber = "BIKE", vehicleTypeId = 3, companyId = 5)
         val result = MockVehicleService.updateVehicleById(1, updatedVehicle)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in update vehicle for vehicle type id as 6 " in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         val updatedVehicle = Vehicle(1, "Bike", modelNumber = "BIKE", vehicleTypeId = 6, companyId = 1)
         val result = MockVehicleService.updateVehicleById(1, updatedVehicle)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in update vehicle if vehicle has duplicate model number " in {
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         val updatedVehicle = Vehicle(1, "TIAGO-R", modelNumber = "TIAGO", vehicleTypeId = 1, companyId = 1)
         val result = MockVehicleService.updateVehicleById(1, updatedVehicle)
         result.failed.futureValue shouldBe an[UniqueKeyViolationException]
      }

      //test cases for groupVehicleByCompany method
      "group vehicles by company in group by company" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         val expectedResult = MockVehicleRepository.vehicleByCompany
         val result = MockVehicleService.groupVehicleByCompany
         result.futureValue shouldBe expectedResult
      }
      "throw exception in group vehicle for empty vehicle list" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.emptyList)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         val result = MockVehicleService.groupVehicleByCompany
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in group vehicle for empty country list" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.emptyList)
         val result = MockVehicleService.groupVehicleByCompany
         result.failed.futureValue shouldBe an[EmptyListException]
      }

      //test cases for getVehiclesByCategory method
      "get vehicles for category id as 1" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         val expectedResult = Seq(MockVehicleRepository.vehicle1, MockVehicleRepository.vehicle3)
         val result = MockVehicleService.getVehiclesByCategory(1)
         result.futureValue shouldBe expectedResult
      }
      "throw exception in get vehicle by category for empty vehicle list" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.emptyList)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         val result = MockVehicleService.getVehiclesByCategory(1)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in get vehicle by category for empty vehicle type list" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.emptyList)
         val result = MockVehicleService.getVehiclesByCategory(1)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in get vehicle by category for category id as 3" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         val result = MockVehicleService.getVehiclesByCategory(3)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }

      //test cases for getVehiclesWithCapacityGreaterThan method
      "return list of vehicles for capacity greater than 25.00" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val expectedResult = Seq(MockVehicleRepository.vehicle2, MockVehicleRepository.vehicle4)
         val result = MockVehicleService.getVehiclesWithCapacityGreaterThan(25.00)
         result.futureValue shouldBe expectedResult
      }
      "throw exception in get vehicle with capacity greater than if vehicle list is empty" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.emptyList)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val result = MockVehicleService.getVehiclesWithCapacityGreaterThan(25.00)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in get vehicle with capacity greater than if vehicle type list is empty" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.emptyList)
         when(MockVehicleService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val result = MockVehicleService.getVehiclesWithCapacityGreaterThan(25.00)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in get vehicle with capacity greater than if vehicle category list is empty" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.emptyList)
         val result = MockVehicleService.getVehiclesWithCapacityGreaterThan(25.00)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in get vehicle with capacity greater than if vehicle is not found" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val result = MockVehicleService.getVehiclesWithCapacityGreaterThan(45.00)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }

      //test cases for getVehicleCountByCountry method
      "return count of vehicle in given country for country id as 1" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         val result = MockVehicleService.getVehicleCountByCountry(1)
         result.futureValue shouldBe 2
      }
      "throw exception in get vehicle count if vehicle list is empty" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.emptyList)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         val result = MockVehicleService.getVehicleCountByCountry(1)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in get vehicle count if company list is empty" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.emptyList)
         val result = MockVehicleService.getVehicleCountByCountry(1)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in get vehicle count if company not found for country id as 3" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         val result = MockVehicleService.getVehicleCountByCountry(3)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }

      //test cases for groupVehiclesByCategory method
      "group vehicle by vehicle category" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val expectedResult = Seq(VehiclesByCategoryContainer("4-Wheeler", Seq(MockVehicleRepository.vehicle1, MockVehicleRepository.vehicle3)),
                                    VehiclesByCategoryContainer("8-Wheeler", Seq(MockVehicleRepository.vehicle2, MockVehicleRepository.vehicle4)))
         val result = MockVehicleService.groupVehiclesByCategory
         result.futureValue shouldBe expectedResult
      }
      "throw exception in group by category if vehicle list is empty" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.emptyList)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val result = MockVehicleService.groupVehiclesByCategory
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in group by category if vehicle type list is empty" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.emptyList)
         when(MockVehicleService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val result = MockVehicleService.groupVehiclesByCategory
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in group by category if vehicle category list is empty" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.emptyList)
         val result = MockVehicleService.groupVehiclesByCategory
         result.failed.futureValue shouldBe an[EmptyListException]
      }

      //test cases for getVehiclesByCompanyOlderThan method
      "return list of vehicle wih company older than 35 years " in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         val result = MockVehicleService.getVehiclesByCompanyOlderThan(35)
         val expectedResult = Seq(MockVehicleRepository.vehicle2, MockVehicleRepository.vehicle4)
         result.futureValue shouldBe expectedResult
      }
      "throw exception if no vehicle found for company older than 40 years" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         val result = MockVehicleService.getVehiclesByCompanyOlderThan(40)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception if vehicle list is empty for company older than 35" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.emptyList)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         val result = MockVehicleService.getVehiclesByCompanyOlderThan(35)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception if company list is empty for company older than 35" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.emptyList)
         val result = MockVehicleService.getVehiclesByCompanyOlderThan(35)
         result.failed.futureValue shouldBe an[EmptyListException]
      }

      // test cases for updateVehicleDetails method
      "update the details of vehicle from vehicle details for id as 1" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleRepository.update(anyLong, any[Vehicle])).thenReturn(Future.successful(1))
         val vehicleDetails = VehicleDetails(30, 30, Some("New description"))
         val result = MockVehicleService.updateVehicleDetails(1, vehicleDetails)
         result.futureValue shouldBe "Updated vehicle details successfully"
      }
      "throw exception in update the details of vehicle from vehicle details for empty vehicle list" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.emptyList)
         when(MockVehicleService.vehicleRepository.update(anyLong, any[Vehicle])).thenReturn(Future.successful(1))
         val vehicleDetails = VehicleDetails(30, 30, Some("New description"))
         val result = MockVehicleService.updateVehicleDetails(1, vehicleDetails)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in update the details of vehicle from vehicle details for vehicle id as 10" in {
         when(MockVehicleService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleRepository.update(anyLong, any[Vehicle])).thenReturn(Future.successful(1))
         val vehicleDetails = VehicleDetails(30, 30, Some("New description"))
         val result = MockVehicleService.updateVehicleDetails(10, vehicleDetails)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }

   }
}

