package com.reactore.feature

/**
  * created by Kartik on 13-11-2017
  */

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.reactore.core.CustomDirectives._
import com.reactore.core._
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class VehicleCategoryRestTestSpec extends WordSpec with Matchers with ScalatestRouteTest {
   val categoryRest     = new VehicleCategoryRest(MockVehicleCategoryService)
   val testRoute: Route = categoryRest.vehicleCategoryRoute
   "Vehicle Category Rest" should {
      "get all vehicle categories" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         Get("/vehiclecategory") ~> testRoute ~> check {
            responseAs[String] shouldBe MockVehicleCategoryRepository.categoryList.asJson
         }
      }
      "throw exception for get all if category list is empty" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.emptyList)
         Get("/vehiclecategory") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Vehicle category list is empty!!".asJson
         }
      }

      "return vehicle category for id as 1" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         Get("/vehiclecategory/1") ~> testRoute ~> check {
            responseAs[String] shouldBe MockVehicleCategoryRepository.vehicleCategory1.asJson
         }
      }
      "throw exception in get by id for category id as 7" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         Get("/vehiclecategory/7") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Vehicle category not found for given id!!".asJson
         }
      }

      "delete vehicle category for id as 3" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         when(MockVehicleCategoryService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleCategoryService.vehicleCategoryRepository.delete(any[Long])).thenReturn(Future.successful(1))
         Delete("/vehiclecategory/3") ~> testRoute ~> check {
            responseAs[String] shouldBe "Deleted vehicle category successfully".asJson
         }
      }
      "throw exception in delete by id for id as 2" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         when(MockVehicleCategoryService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleCategoryService.vehicleCategoryRepository.delete(any[Long])).thenReturn(Future.successful(1))
         Delete("/vehiclecategory/2") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Foreign key relation found in vehicle type table!!".asJson
         }
      }
      "throw exception in delete by id if category list as empty" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.emptyList)
         when(MockVehicleCategoryService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleCategoryService.vehicleCategoryRepository.delete(any[Long])).thenReturn(Future.successful(1))
         Delete("/vehiclecategory/3") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Vehicle category list is empty!!".asJson
         }
      }

      "insert new vehicle category" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         when(MockVehicleCategoryService.vehicleCategoryRepository.insert(any[VehicleCategory])).thenReturn(Future.successful(1))
         val newCategory = VehicleCategory(4, "3-Wheeler", maxCapacity = 2.0).asJson
         Post("/vehiclecategory").withEntity(newCategory) ~> testRoute ~> check {
            responseAs[String] shouldBe "Inserted vehicle category successfully".asJson
         }
      }
      "throw exception if category name is duplicated" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val newCategory = VehicleCategory(4, "4-Wheeler", maxCapacity = 15.0).asJson
         Post("/vehiclecategory").withEntity(newCategory) ~> testRoute ~> check {
            responseAs[String] shouldBe "Category Name already defined!!".asJson
         }
      }
      "update the vehicle category for category id as 1" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         when(MockVehicleCategoryService.vehicleCategoryRepository.update(any[Long], any[VehicleCategory])).thenReturn(Future.successful(1))
         val updatedCategory = VehicleCategory(1, "10-Wheeler", maxCapacity = 2.0).asJson
         Put("/vehiclecategory/1").withEntity(updatedCategory) ~> testRoute ~> check {
            responseAs[String] shouldBe "Updated vehicle category successfully".asJson
         }
      }
      "throw exception in update for empty category list" in {
         when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.emptyList)
         val updatedCategory = VehicleCategory(1, "10-Wheeler", maxCapacity = 2.0).asJson
         Put("/vehiclecategory/1").withEntity(updatedCategory) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Vehicle category list is empty!!".asJson
         }
      }


   }
}
