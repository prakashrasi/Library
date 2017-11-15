package com.reactore.feature

import com.reactore.core._
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

/**
  * created by Kartik on 12-11-2017
  */
class VehicleCategoryServiceTestSpec extends WordSpec with Matchers with ScalaFutures {
   "Vehicle Category Service" should {
      // test cases for getAll method
      "return all category list " in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val result = MockVehicleCategoryService.getAll
         val expectedResult = MockVehicleCategoryRepository.categoryList
         result.futureValue shouldBe expectedResult
      }
      "throw exception in get all for empty category list" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.emptyList)
         val result = MockVehicleCategoryService.getAll
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      // test cases for getVehicleCategoryById method
      "return vehicle category in get category by id for id as 1" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val expectedResult = MockVehicleCategoryRepository.vehicleCategory1
         val result = MockVehicleCategoryService.getVehicleCategoryById(1)
         result.futureValue shouldBe expectedResult
      }
      "throw exception in get category by id for id as 5" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val result = MockVehicleCategoryService.getVehicleCategoryById(5)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in get category for empty category list" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.emptyList)
         val result = MockVehicleCategoryService.getVehicleCategoryById(1)
         result.failed.futureValue shouldBe an[EmptyListException]
      }

      //test cases for insertVehicleCategory method
      "insert vehicle into vehicle category table " in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         when(MockVehicleCategoryService.vehicleCategoryRepository.insert(any[VehicleCategory])).thenReturn(Future.successful(1))
         val newCategory = VehicleCategory(4, "3-Wheeler", maxCapacity = 2.0)
         val result = MockVehicleCategoryService.insertVehicleCategory(newCategory)
         result.futureValue shouldBe "Inserted vehicle category successfully"
      }
      "throw exception in insert category if category name is undefined" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val newCategory = VehicleCategory(4, "", maxCapacity = 2.0)
         val result = MockVehicleCategoryService.insertVehicleCategory(newCategory)
         result.failed.futureValue shouldBe an[FieldNotDefinedException]
      }
      "throw exception in insert category if category name is duplicated" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val newCategory = VehicleCategory(4, "4-Wheeler", maxCapacity = 15.0)
         val result = MockVehicleCategoryService.insertVehicleCategory(newCategory)
         result.failed.futureValue shouldBe an[DuplicateEntityException]
      }

      //test cases for deleteVehicleCategoryById method
      "delete vehicle category for category id as 3" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         when(MockVehicleCategoryService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleCategoryService.vehicleCategoryRepository.delete(any[Long])).thenReturn(Future.successful(1))
         val result = MockVehicleCategoryService.deleteVehicleCategoryById(3)
         result.futureValue shouldBe "Deleted vehicle category successfully"
      }
      "throw exception in delete vehicle category for category id as 4" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         when(MockVehicleCategoryService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         val result = MockVehicleCategoryService.deleteVehicleCategoryById(4)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in delete vehicle category for empty vehicle category list" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.emptyList)
         when(MockVehicleCategoryService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         val result = MockVehicleCategoryService.deleteVehicleCategoryById(1)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in delete vehicle category if foreign key relation exists" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         when(MockVehicleCategoryService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         val result = MockVehicleCategoryService.deleteVehicleCategoryById(1)
         result.failed.futureValue shouldBe an[ForeignKeyRelationFoundException]
      }

      //test cases for updateVehicleCategoryById method
      "update the vehicle category for category id as 1" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         when(MockVehicleCategoryService.vehicleCategoryRepository.update(any[Long], any[VehicleCategory])).thenReturn(Future.successful(1))
         val updatedCategory = VehicleCategory(1, "10-Wheeler", maxCapacity = 2.0)
         val result = MockVehicleCategoryService.updateVehicleCategoryById(1, updatedCategory)
         result.futureValue shouldBe "Updated vehicle category successfully"
      }
      "throw exception in update category if name is undefined" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val updatedCategory = VehicleCategory(1, "", maxCapacity = 2.0)
         val result = MockVehicleCategoryService.updateVehicleCategoryById(1, updatedCategory)
         result.failed.futureValue shouldBe an[FieldNotDefinedException]
      }
      "throw exception in update category for category id as 5" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val updatedCategory = VehicleCategory(5, "15-Wheeler", maxCapacity = 2.0)
         val result = MockVehicleCategoryService.updateVehicleCategoryById(5, updatedCategory)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in update category for empty category list" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.emptyList)
         val updatedCategory = VehicleCategory(2, "15-Wheeler", maxCapacity = 2.0)
         val result = MockVehicleCategoryService.updateVehicleCategoryById(2, updatedCategory)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
   }
}

