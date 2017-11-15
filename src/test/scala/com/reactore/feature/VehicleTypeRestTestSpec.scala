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

class VehicleTypeRestTestSpec extends WordSpec with Matchers with ScalatestRouteTest {
   val vehicleTypeRest  = new VehicleTypeRest(MockVehicleTypeService)
   val testRoute: Route = vehicleTypeRest.vehicleTypeRoute
   "Vehicle Type Rest" should {
      "get all vehicle types" in {
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         Get("/vehicletype") ~> testRoute ~> check {
            responseAs[String] shouldBe MockVehicleTypeRepository.vehicleTypeList.asJson
         }
      }
      "throw exception for get all if list is empty" in {
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.emptyList)
         Get("/vehicletype") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Vehicle type list is empty!!".asJson
         }
      }

      "get a vehicle type for type id as 1" in {
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         Get("/vehicletype/1") ~> testRoute ~> check {
            responseAs[String] shouldBe MockVehicleTypeRepository.vehicleType1.asJson
         }
      }
      "throw exception for get by id if type list is empty" in {
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.emptyList)
         Get("/vehicletype/1") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Vehicle type list is empty!!".asJson
         }
      }
      "throw exception for get by id for type id as 9" in {
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         Get("/vehicletype/9") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Vehicle type not found for given id!!".asJson
         }
      }

      "delete vehicle type for id as 5" in {
         when(MockVehicleTypeService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.delete(anyLong)).thenReturn(Future.successful(1))
         Delete("/vehicletype/5") ~> testRoute ~> check {
            responseAs[String] shouldBe "Deleted vehicle type successfully".asJson
         }
      }
      "throw exception in delete type if vehicle type list is empty" in {
         when(MockVehicleTypeService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.emptyList)
         Delete("/vehicletype/5") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Vehicle type list is empty!!".asJson
         }
      }
      "throw exception in delete type if vehicle type id does not exists" in {
         when(MockVehicleTypeService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         Delete("/vehicletype/9") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Vehicle type not found for given id!!".asJson
         }
      }
      "throw exception in delete type if foreign key relation exists" in {
         when(MockVehicleTypeService.vehicleRepository.getAllVehicles).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         Delete("/vehicletype/1") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Foreign key relation found in vehicle table!!".asJson
         }
      }

      "insert a new vehicle type into list " in {
         when(MockVehicleTypeService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.insert(any[VehicleType])).thenReturn(Future.successful(1))
         val newType = VehicleType(6, "Bus", vehicleCategoryId = 2).asJson
         Post("/vehicletype").withEntity(newType) ~> testRoute ~> check {
            responseAs[String] shouldBe "Inserted vehicle type successfully".asJson
         }
      }
      "throw exception in insert vehicle type if name is not defined" in {
         when(MockVehicleTypeService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         val newType = VehicleType(6, "", vehicleCategoryId = 2).asJson
         Post("/vehicletype").withEntity(newType) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Name is not defined!!".asJson
         }
      }
      "throw exception in insert type if category id does not exists" in {
         when(MockVehicleTypeService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         val newType = VehicleType(6, "Bus", vehicleCategoryId = 4).asJson
         Post("/vehicletype").withEntity(newType) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Category does not exists!!".asJson
         }
      }
      "throw exception in insert type if category list is empty" in {
         when(MockVehicleTypeService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.emptyList)
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         val newType = VehicleType(6, "Bus", vehicleCategoryId = 2).asJson
         Post("/vehicletype").withEntity(newType) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Category list is empty!!".asJson
         }
      }
      "throw exception in insert type if vehicle type already defined" in {
         when(MockVehicleTypeService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         val newType = VehicleType(6, "Car", vehicleCategoryId = 1).asJson
         Post("/vehicletype").withEntity(newType) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Vehicle Type already defined!!".asJson
         }
      }

      "update the vehicle type details" in {
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleTypeService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         when(MockVehicleTypeService.vehicleTypeRepository.update(anyLong, any[VehicleType])).thenReturn(Future.successful(1))
         val updatedType = VehicleType(3, "Big Van", vehicleCategoryId = 1).asJson
         Put("/vehicletype/3").withEntity(updatedType) ~> testRoute ~> check {
            responseAs[String] shouldBe "Updated vehicle type successfully".asJson
         }
      }
      "throw exception in update vehicle type if vehicle type list is empty" in {
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.emptyList)
         when(MockVehicleTypeService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val updatedType = VehicleType(3, "Big Van", vehicleCategoryId = 1).asJson
         Put("/vehicletype/3").withEntity(updatedType) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Vehicle type list is empty!!".asJson
         }
      }
      "throw exception in update vehicle type for vehicle type id as 10" in {
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleTypeService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val updatedType = VehicleType(10, "Big Van", vehicleCategoryId = 1).asJson
         Put("/vehicletype/10").withEntity(updatedType) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Vehicle type not found given id!!".asJson
         }
      }
      "throw exception in update vehicle type for vehicle category id as 4" in {
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleTypeService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val updatedType = VehicleType(3, "Big Van", vehicleCategoryId = 4).asJson
         Put("/vehicletype/3").withEntity(updatedType) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Vehicle Category not found for updated vehicle type!!".asJson
         }
      }
      "throw exception in update vehicle type if fields are not defined" in {
         when(MockVehicleTypeService.vehicleTypeRepository.getAllVehicleTypes).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleTypeService.vehicleCategoryRepository.getAllCategory).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val updatedType = VehicleType(3, "", vehicleCategoryId = 2).asJson
         Put("/vehicletype/3").withEntity(updatedType) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Fields are not defined!!".asJson
         }
      }

   }

}
