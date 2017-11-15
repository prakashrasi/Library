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

class VehicleRestTestSpec extends WordSpec with ScalatestRouteTest with Matchers {
   val vehicleRest      = new VehicleRest(MockVehicleService)
   val testRoute: Route = vehicleRest.vehicleRoute
   "Vehicle Rest" should {
      "get all vehicles" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         val expectedResult = MockVehicleRepository.vehicleList.asJson
         Get("/vehicle") ~> testRoute ~> check {
            responseAs[String] shouldBe expectedResult
         }
      }
      "throw exception for get all for empty vehicle list" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.emptyList)
         Get("/vehicle") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Vehicle list is empty!!".asJson
         }
      }
      "get vehicle in get by id for vehicle id as 1" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         val expectedResult = MockVehicleRepository.vehicle1.asJson
         Get("/vehicle/1") ~> testRoute ~> check {
            responseAs[String] shouldBe expectedResult
         }
      }
      "throw exception in get vehicle for vehicle id as 5" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         Get("/vehicle/5") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Vehicle not found for given id !!".asJson
         }
      }
      "throw exception in get vehicle for empty vehicle list" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.emptyList)
         Get("/vehicle/1") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Vehicle list is empty!!".asJson
         }
      }

      "insert a new vehicle " in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         when(MockVehicleService.vehicleRepository.insert(any[Vehicle])).thenReturn(Future.successful(1))
         val newVehicle = Vehicle(5, "Bike", modelNumber = "BIKE", vehicleTypeId = 3, companyId = 1).asJson
         Post("/vehicle").withEntity(newVehicle) ~> testRoute ~> check {
            responseAs[String] shouldBe "Inserted vehicle successfully".asJson
         }
      }
      "throw exception in insert vehicle if fields are not defined" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val newVehicle = Vehicle(5, "", modelNumber = "", vehicleTypeId = 3, companyId = 1).asJson
         Post("/vehicle").withEntity(newVehicle) ~> testRoute ~> check {
            responseAs[String] shouldBe "Fields are not defined!!".asJson
         }
      }
      "throw exception in insert vehicle if company list is empty" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.emptyList)
         val newVehicle = Vehicle(5, "Bike", modelNumber = "BIKE", vehicleTypeId = 3, companyId = 1).asJson
         Post("/vehicle").withEntity(newVehicle) ~> testRoute ~> check {
            responseAs[String] shouldBe "Company list is empty!!".asJson
         }
      }
      "throw exception in insert vehicle if vehicle type list is empty" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.emptyList)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val newVehicle = Vehicle(5, "Bike", modelNumber = "BIKE", vehicleTypeId = 3, companyId = 1).asJson
         Post("/vehicle").withEntity(newVehicle) ~> testRoute ~> check {
            responseAs[String] shouldBe "Vehicle type list is empty!!".asJson
         }
      }
      "throw exception in insert vehicle if company does not exists" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val newVehicle = Vehicle(5, "Bike", modelNumber = "BIKE", vehicleTypeId = 3, companyId = 4).asJson
         Post("/vehicle").withEntity(newVehicle) ~> testRoute ~> check {
            responseAs[String] shouldBe "Company not found!!".asJson
         }
      }
      "throw exception in insert vehicle if vehicle type does not exists" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val newVehicle = Vehicle(5, "Bike", modelNumber = "BIKE", vehicleTypeId = 6, companyId = 2).asJson
         Post("/vehicle").withEntity(newVehicle) ~> testRoute ~> check {
            responseAs[String] shouldBe "Vehicle type not found!!".asJson
         }
      }
      "throw exception in insert vehicle if duplicate model number inserted" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val newVehicle = Vehicle(5, "Winger", modelNumber = "WINGER", vehicleTypeId = 4, companyId = 2).asJson
         Post("/vehicle").withEntity(newVehicle) ~> testRoute ~> check {
            responseAs[String] shouldBe "Unique model number violated!!".asJson
         }
      }

      "delete vehicle in delete vehicle for id 1" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleRepository.delete(anyLong)).thenReturn(Future.successful(1))
         Delete("/vehicle/1") ~> testRoute ~> check {
            responseAs[String] shouldBe "Deleted vehicle successfully".asJson
         }
      }
      "throw exception in delete vehicle if vehicle list is empty" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.emptyList)
         Delete("/vehicle/1") ~> testRoute ~> check {
            responseAs[String] shouldBe "Vehicle list is empty!!".asJson
         }
      }
      "throw exception in delete vehicle if vehicle id not found for id as 6" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         Delete("/vehicle/6") ~> testRoute ~> check {
            responseAs[String] shouldBe "Vehicle not found for given id !!".asJson
         }
      }

      "update vehicle details" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         when(MockVehicleService.vehicleRepository.update(anyLong, any[Vehicle])).thenReturn(Future.successful(1))
         val updatedVehicle = Vehicle(1, "Bike", modelNumber = "BIKE", vehicleTypeId = 3, companyId = 3).asJson
         Put("/vehicle/1").withEntity(updatedVehicle) ~> testRoute ~> check {
            responseAs[String] shouldBe "Updated vehicle successfully".asJson
         }
      }
      "throw exception in update vehicle if fields are not defined" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val updatedVehicle = Vehicle(1, "", modelNumber = "", vehicleTypeId = 3, companyId = 3).asJson
         Put("/vehicle/1").withEntity(updatedVehicle) ~> testRoute ~> check {
            responseAs[String] shouldBe "All fields are not defined!!".asJson
         }
      }
      "throw exception in update vehicle if vehicle id does not exists for id as 5 " in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val updatedVehicle = Vehicle(5, "Bike", modelNumber = "BIKE", vehicleTypeId = 3, companyId = 3).asJson
         Put("/vehicle/5").withEntity(updatedVehicle) ~> testRoute ~> check {
            responseAs[String] shouldBe "Vehicle not found for given id!!".asJson
         }
      }
      "throw exception in update vehicle for company id as 5 " in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val updatedVehicle = Vehicle(1, "Bike", modelNumber = "BIKE", vehicleTypeId = 3, companyId = 5).asJson
         Put("/vehicle/1").withEntity(updatedVehicle) ~> testRoute ~> check {
            responseAs[String] shouldBe "Company not found for updated data!!".asJson
         }
      }
      "throw exception in update vehicle for vehicle type id as 6 " in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val updatedVehicle = Vehicle(1, "Bike", modelNumber = "BIKE", vehicleTypeId = 6, companyId = 1).asJson
         Put("/vehicle/1").withEntity(updatedVehicle) ~> testRoute ~> check {
            responseAs[String] shouldBe "Vehicle Type not found for updated data!!".asJson
         }
      }
      "throw exception in update vehicle if vehicle has duplicate model number " in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val updatedVehicle = Vehicle(1, "TIAGO-R", modelNumber = "TIAGO", vehicleTypeId = 1, companyId = 1).asJson
         Put("/vehicle/1").withEntity(updatedVehicle) ~> testRoute ~> check {
            responseAs[String] shouldBe "Updated vehicle has duplicate model number!!".asJson
         }
      }

      "group vehicles by company in group by company" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val expectedResult = MockVehicleRepository.vehicleByCompany.asJson
         Get("/vehicle/company") ~> testRoute ~> check {
            responseAs[String] shouldBe expectedResult
         }
      }
      "throw exception in group vehicle for empty vehicle list" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.emptyList)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         Get("/vehicle/company") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }
      "throw exception in group vehicle for empty country list" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.emptyList)
         Get("/vehicle/company") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }

      "group vehicle by vehicle category" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val expectedResult = Seq(VehiclesByCategoryContainer("4-Wheeler", Seq(MockVehicleRepository.vehicle1, MockVehicleRepository.vehicle3)),
                                    VehiclesByCategoryContainer("8-Wheeler", Seq(MockVehicleRepository.vehicle2, MockVehicleRepository.vehicle4))).asJson
         Get("/vehicle/category") ~> testRoute ~> check {
            responseAs[String] shouldBe expectedResult
         }
      }
      "throw exception in group by category if vehicle list is empty" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.emptyList)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         Get("/vehicle/category") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }
      "throw exception in group by category if vehicle type list is empty" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.emptyList)
         when(MockVehicleService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         Get("/vehicle/category") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }
      "throw exception in group by category if vehicle category list is empty" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.emptyList)
         Get("/vehicle/category") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }

      "get vehicles for category id as 1" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         val expectedResult = Seq(MockVehicleRepository.vehicle1, MockVehicleRepository.vehicle3).asJson
         Get("/vehicle/category/1") ~> testRoute ~> check {
            responseAs[String] shouldBe expectedResult
         }
      }
      "throw exception in get vehicle by category for empty vehicle list" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.emptyList)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         Get("/vehicle/category/1") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }
      "throw exception in get vehicle by category for empty vehicle type list" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.emptyList)
         Get("/vehicle/category/1") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }
      "throw exception in get vehicle by category for category id as 3" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         Get("/vehicle/category/3") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }

      "return count of vehicle in given country for country id as 1" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         Get("/vehicle/countbycountry/1") ~> testRoute ~> check {
            responseAs[String] shouldEqual toJson(2)
         }
      }
      "throw exception in get vehicle count if vehicle list is empty" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.emptyList)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         Get("/vehicle/countbycountry/1") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }
      "throw exception in get vehicle count if company list is empty" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.emptyList)
         Get("/vehicle/countbycountry/1") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }
      "throw exception in get vehicle count if company not found for country id as 3" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         Get("/vehicle/countbycountry/3") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }

      "return list of vehicle wih company older than 35 years " in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val expectedResult = Seq(MockVehicleRepository.vehicle2, MockVehicleRepository.vehicle4).asJson
         Get("/vehicle/company/35") ~> testRoute ~> check {
            responseAs[String] shouldBe expectedResult
         }
      }
      "throw exception if no vehicle found for company older than 40 years" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         Get("/vehicle/company/40") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }
      "throw exception if vehicle list is empty for company older than 35" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.emptyList)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         Get("/vehicle/company/35") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }
      "throw exception if company list is empty for company older than 35" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.emptyList)
         Get("/vehicle/company/35") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }

      "return list of vehicles for capacity greater than 25.00" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         val expectedResult = Seq(MockVehicleRepository.vehicle2, MockVehicleRepository.vehicle4).asJson
         Get("/vehicle/capacity/25") ~> testRoute ~> check {
            responseAs[String] shouldBe expectedResult
         }
      }
      "throw exception in get vehicle with capacity greater than if vehicle list is empty" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.emptyList)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         Get("/vehicle/capacity/25") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }
      "throw exception in get vehicle with capacity greater than if vehicle type list is empty" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.emptyList)
         when(MockVehicleService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         Get("/vehicle/capacity/25") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }
      "throw exception in get vehicle with capacity greater than if vehicle category list is empty" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.emptyList)
         Get("/vehicle/capacity/25") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }
      "throw exception in get vehicle with capacity greater than if vehicle is not found" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         when(MockVehicleService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(MockVehicleCategoryRepository.vehicleCategoryFuture)
         Get("/vehicle/capacity/45") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }

      "update the details of vehicle from vehicle details for id as 1" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleRepository.update(anyLong, any[Vehicle])).thenReturn(Future.successful(1))
         val vehicleDetails = VehicleDetails(30, 30, Some("New description")).asJson
         Put("/vehicle/details/1").withEntity(vehicleDetails) ~> testRoute ~> check {
            responseAs[String] shouldBe "Updated vehicle details successfully".asJson
         }
      }
      "throw exception in update the details of vehicle from vehicle details for vehicle id as 10" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockVehicleService.vehicleRepository.update(anyLong, any[Vehicle])).thenReturn(Future.successful(1))
         val vehicleDetails = VehicleDetails(30, 30, Some("New description")).asJson
         Put("/vehicle/details/10").withEntity(vehicleDetails) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }
      "throw exception in update the details of vehicle from vehicle details for empty vehicle list" in {
         when(MockVehicleService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.emptyList)
         when(MockVehicleService.vehicleRepository.update(anyLong, any[Vehicle])).thenReturn(Future.successful(1))
         val vehicleDetails = VehicleDetails(30, 30, Some("New description")).asJson
         Put("/vehicle/details/1").withEntity(vehicleDetails) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }


   }

}
