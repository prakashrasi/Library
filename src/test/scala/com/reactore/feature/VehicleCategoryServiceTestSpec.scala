package com.reactore.feature

import com.reactore.core._
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

/**
  * created by Kartik on 12-11-2017
  */
class VehicleCategoryServiceTestSpec extends WordSpec with Matchers with ScalaFutures {
   "Vehicle Category Service" should {
      "return vehicle category in get category by id for id as 1" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val expectedResult = MockVehicleCategoryRepository.vehicleCategory1
         val result = MockVehicleCategoryService.getVehicleCategoryById(1)
         result.futureValue shouldBe expectedResult
      }
      "throw exception in get category by id for id as 5" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val result = MockVehicleCategoryService.getVehicleCategoryById(5)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in get category for empty category list" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.emptyList)
         val result = MockVehicleCategoryService.getVehicleCategoryById(1)
         result.failed.futureValue shouldBe an[EmptyListException]
      }

   }
}

object MockVehicleCategoryService extends VehicleCategoryService with MockVehicleCategoryFacade
