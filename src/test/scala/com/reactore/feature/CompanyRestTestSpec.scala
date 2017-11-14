package com.reactore.feature

import java.sql.Timestamp

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.reactore.core.CustomDirectives._
import com.reactore.core._
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

/**
  * created by Kartik on 13-11-2017
  */
class CompanyRestTestSpec extends WordSpec with Matchers with ScalatestRouteTest {
   val companyRest      = new CompanyRest(MockCompanyService)
   val testRoute: Route = companyRest.companyRoute
   "Company Rest" should {
      "return a company list" in {
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         Get("/company") ~> testRoute ~> check {
            responseAs[String] shouldBe MockCompanyRepository.companyList.asJson
         }
      }
      "throw exception in get all for empty company list" in {
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.emptyList)
         Get("/company") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Company list is empty!!".asJson
         }
      }

      "return company for company id as 2" in {
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         Get("/company/2") ~> testRoute ~> check {
            responseAs[String] shouldBe MockCompanyRepository.company2.asJson
         }
      }
      "throw exception in get company by id for company id 5" in {
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         Get("/company/5") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Company not found!!".asJson
         }
      }
      "throw exception for empty company list" in {
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.emptyList)
         Get("/company/1") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Company list is empty!!".asJson
         }
      }

      "insert a company to database" in {
         val newCompany = Company(4, "RENAULT", licenceNumber = "REN001", country = 2,startYear = Timestamp.valueOf("1970-01-01 00:00:00")).asJson
         when(MockCompanyService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         when(MockCompanyService.companyRepository.insert(any[Company])).thenReturn(Future.successful(1))
         Post("/company").withEntity(newCompany) ~> testRoute ~> check {
            responseAs[String] shouldBe "Inserted company successfully!!".asJson
         }
      }
      "throw exception if country list is empty" in {
         val newCompany = Company(4, "RENAULT", licenceNumber = "REN001", country = 2,startYear = Timestamp.valueOf("1970-01-01 00:00:00")).asJson
         when(MockCompanyService.countryRepository.countryFuture).thenReturn(MockCountryRepository.emptyList)
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         when(MockCompanyService.companyRepository.insert(any[Company])).thenReturn(Future.successful(1))
         Post("/company").withEntity(newCompany) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Country list is Empty!!".asJson
         }
      }
      "throw exception in insert company if name and licence number not defined" in {
         when(MockCompanyService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val newCompany = Company(4, "", licenceNumber = "", country = 3,startYear = Timestamp.valueOf("1970-01-01 00:00:00")).asJson
         Post("/company").withEntity(newCompany) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }
      "throw exception in insert company if country does not exists" in {
         when(MockCompanyService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val newCompany = Company(4, "RENAULT", licenceNumber = "REN001", country = 4,startYear = Timestamp.valueOf("1970-01-01 00:00:00")).asJson
         Post("/company").withEntity(newCompany) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }
      "throw exception for insert company if duplicate country is inserted" in {
         when(MockCompanyService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val newCompany = Company(1, "TATA", licenceNumber = "TA001", country = 3,startYear = Timestamp.valueOf("1970-01-01 00:00:00")).asJson
         Post("/company").withEntity(newCompany) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }

      "delete company for company id as 3" in {
         when(MockCompanyService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         when(MockCompanyService.companyRepository.delete(anyLong)).thenReturn(Future.successful(1))
         Delete("/company/3") ~> testRoute ~> check {
            responseAs[String] shouldBe "Deleted company successfully".asJson
         }
      }
      "throw exception in delete route for empty company list" in {
         when(MockCompanyService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.emptyList)
         Delete("/company/3") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Company list is empty!!".asJson
         }
      }
      "throw exception in delete company for company id as 5" in {
         when(MockCompanyService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         Delete("/company/5") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }
      "throw exception in delete company if foreign key relation exists" in {
         when(MockCompanyService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         Delete("/company/1") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }

      "update company details" in {
         val updatedCompany = Company(2, "Scania", licenceNumber = "SCAN001", country = 2,startYear = Timestamp.valueOf("1970-01-01 00:00:00")).asJson
         when(MockCompanyService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         when(MockCompanyService.companyRepository.update(anyLong, any[Company])).thenReturn(Future.successful(1))
         Put("/company/2").withEntity(updatedCompany) ~> testRoute ~> check {
            responseAs[String] shouldBe "Updated company successfully".asJson
         }
      }
      "throw exception company id as 5" in {
         val updatedCompany = Company(5, "Scania", licenceNumber = "SCAN001", country = 2,startYear = Timestamp.valueOf("1970-01-01 00:00:00")).asJson
         when(MockCompanyService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         Put("/company/5").withEntity(updatedCompany) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "No company found for given id!!".asJson
         }
      }
      "throw exception if country is not found" in {
         val updatedCompany = Company(2, "Scania", licenceNumber = "SCAN001", country = 5,startYear = Timestamp.valueOf("1970-01-01 00:00:00")).asJson
         when(MockCompanyService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         Put("/company/2").withEntity(updatedCompany) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "No country found!!".asJson
         }
      }
      "throw exception in update company if name and licence number not defined" in {
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         when(MockCompanyService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         val updatedCompany = Company(1, "", licenceNumber = "", country = 1,startYear = Timestamp.valueOf("1970-01-01 00:00:00")).asJson
         Put("/company/1").withEntity(updatedCompany) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }
      "throw exception in update company for duplicate company details" in {
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         when(MockCompanyService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         val updatedCompany = Company(1, "TATA", licenceNumber = "TA001", country = 1,startYear = Timestamp.valueOf("1970-01-01 00:00:00")).asJson
         Put("/company/1").withEntity(updatedCompany) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }
   }
}
