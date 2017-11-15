package com.reactore.feature

/**
  * created by Kartik on 13-11-2017
  */

import com.reactore.core._
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.Future
class VehicleTypeServiceTestSpec extends WordSpec with Matchers with ScalaFutures {
   "Vehicle Type Service" should {
      // test cases for getAll method
      "return all vehicle type list " in {
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         val result = MockVehicleTypeService.getAll
         val expectedResult = MockVehicleTypeRepository.vehicleTypeList
         result.futureValue shouldBe expectedResult
      }
      "throw exception in get all for empty vehicle type list" in {
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.emptyList)
         val result = MockVehicleTypeService.getAll
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      //test cases for getVehicleTypeById method
      "get a vehicle type for type id as 1" in {
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         val expectedResult = MockVehicleTypeRepository.vehicleType1
         val result = MockVehicleTypeService.getVehicleTypeById(1)
         result.futureValue shouldBe expectedResult
      }
      "throw exception in get type id for empty vehicle type list" in {
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.emptyList)
         val result = MockVehicleTypeService.getVehicleTypeById(1)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in get type id for vehicle type id as 8" in {
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         val result = MockVehicleTypeService.getVehicleTypeById(8)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }

      //test cases for insertVehicleType method
      "insert a new vehicle type " in {
         when(MockVehicleTypeService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.insert(any[VehicleType])).thenReturn(Future.successful(1))
         val newType = VehicleType(6, "Bus", vehicleCategoryId = 2)
         val result = MockVehicleTypeService.insertVehicleType(newType)
         result.futureValue shouldBe "Inserted vehicle type successfully"
      }
      "throw exception in insert vehicle type if name is not defined" in {
         when(MockVehicleTypeService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         val newType = VehicleType(6, "", vehicleCategoryId = 2)
         val result = MockVehicleTypeService.insertVehicleType(newType)
         result.failed.futureValue shouldBe an[FieldNotDefinedException]
      }
      "throw exception in insert type if category id does not exists" in {
         when(MockVehicleTypeService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         val newType = VehicleType(6, "Bus", vehicleCategoryId = 4)
         val result = MockVehicleTypeService.insertVehicleType(newType)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in insert type if category list is empty" in {
         when(MockVehicleTypeService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.emptyList)
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         val newType = VehicleType(6, "Bus", vehicleCategoryId = 2)
         val result = MockVehicleTypeService.insertVehicleType(newType)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in insert type if vehicle type already defined" in {
         when(MockVehicleTypeService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         val newType = VehicleType(6, "Car", vehicleCategoryId = 1)
         val result = MockVehicleTypeService.insertVehicleType(newType)
         result.failed.futureValue shouldBe an[DuplicateEntityException]
      }

      //test cases for deleteVehicleTypeById method
      "delete vehicle type for vehicle type id as 5" in {
         when(MockVehicleTypeService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.delete(anyLong)).thenReturn(Future.successful(1))
         val result = MockVehicleTypeService.deleteVehicleTypeById(5)
         result.futureValue shouldBe "Deleted vehicle type successfully"
      }
      "throw exception in delete type if vehicle type list is empty" in {
         when(MockVehicleTypeService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.emptyList)
         val result = MockVehicleTypeService.deleteVehicleTypeById(5)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in delete type if vehicle type id does not exists" in {
         when(MockVehicleTypeService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         val result = MockVehicleTypeService.deleteVehicleTypeById(9)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in delete type if foreign key relation exists" in {
         when(MockVehicleTypeService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         val result = MockVehicleTypeService.deleteVehicleTypeById(1)
         result.failed.futureValue shouldBe an[ForeignKeyRelationFoundException]
      }

      //test cases for updateVehicleTypeById method
      "update the vehicle type details" in {
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleTypeService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.update(anyLong, any[VehicleType])).thenReturn(Future.successful(1))
         val updatedType = VehicleType(3, "Big Van", vehicleCategoryId = 1)
         val result = MockVehicleTypeService.updateVehicleTypeById(3, updatedType)
         result.futureValue shouldBe "Updated vehicle type successfully"
      }
      "throw exception in update vehicle type if vehicle type list is empty" in {
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.emptyList)
         when(MockVehicleTypeService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val updatedType = VehicleType(3, "Big Van", vehicleCategoryId = 1)
         val result = MockVehicleTypeService.updateVehicleTypeById(3, updatedType)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in update vehicle type for vehicle type id as 10" in {
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleTypeService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val updatedType = VehicleType(10, "Big Van", vehicleCategoryId = 1)
         val result = MockVehicleTypeService.updateVehicleTypeById(10, updatedType)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in update vehicle type for vehicle category id as 4" in {
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleTypeService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val updatedType = VehicleType(3, "Big Van", vehicleCategoryId = 4)
         val result = MockVehicleTypeService.updateVehicleTypeById(3, updatedType)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in update vehicle type if fields are not defined" in {
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleTypeService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val updatedType = VehicleType(3, "", vehicleCategoryId = 2)
         val result = MockVehicleTypeService.updateVehicleTypeById(3, updatedType)
         result.failed.futureValue shouldBe an[FieldNotDefinedException]
      }
   }
}

