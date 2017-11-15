package com.reactore.feature

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
class CountryRestTestSpec extends WordSpec with Matchers with ScalatestRouteTest {
   val countryRest      = new CountryRest(MockCountryService)
   val testRoute: Route = countryRest.countryRoute
   "Country Rest" should {
      "return list of countries" in {
         when(MockCountryService.countryRepository.getAllCountry).thenReturn(MockCountryRepository.countryFuture)
         Get("/country") ~> testRoute ~> check {
            responseAs[String] shouldBe MockCountryRepository.countryList.asJson
         }
      }
      "throw exception in get if country list is empty" in {
         when(MockCountryService.countryRepository.getAllCountry).thenReturn(MockCountryRepository.emptyList)
         Get("/country") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Country list is empty!!".asJson
         }
      }

      "return a country for id as 1" in {
         when(MockCountryService.countryRepository.getAllCountry).thenReturn(MockCountryRepository.countryFuture)
         Get("/country/1") ~> testRoute ~> check {
            responseAs[String] shouldBe MockCountryRepository.country1.asJson
         }
      }
      "throw exception for get country by id for country id as 6" in {
         when(MockCountryService.countryRepository.getAllCountry).thenReturn(MockCountryRepository.countryFuture)
         Get("/country/6") ~> testRoute ~> check {
            responseAs[String] shouldBe "Country not found!!".asJson
         }
      }
      "throw exception for get country by id for empty country list" in {
         when(MockCountryService.countryRepository.getAllCountry).thenReturn(MockCountryRepository.emptyList)
         Get("/country/6") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }
      "insert country to list" in {
         val newCountry = Country(4, "FRANCE", language = "FRENCH", code = "FRA").asJson
         when(MockCountryService.countryRepository.getAllCountry).thenReturn(MockCountryRepository.countryFuture)
         when(MockCountryService.countryRepository.insert(any[Country])).thenReturn(Future.successful(1))
         Post("/country").withEntity(newCountry) ~> testRoute ~> check {
            responseAs[String] shouldBe "Inserted country successfully".asJson
         }
      }
      "throw exception in insert if duplicate code is inserted" in {
         val newCountry = Country(4, "INDONESIA", language = "FRENCH", code = "IND").asJson
         when(MockCountryService.countryRepository.getAllCountry).thenReturn(MockCountryRepository.countryFuture)
         Post("/country").withEntity(newCountry) ~> testRoute ~> check {
            status shouldEqual StatusCodes.Conflict
            responseAs[String] shouldBe "Unique country code violated!!".asJson
         }
      }
      "throw exception in insert company for country name and language not defined" in {
         when(MockCountryService.countryRepository.getAllCountry).thenReturn(MockCountryRepository.countryFuture)
         val newCountry = Country(4, "", "", "XYZ").asJson
         Post("/country").withEntity(newCountry) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }

      "delete country by id for id as 3" in {
         when(MockCountryService.countryRepository.getAllCountry).thenReturn(MockCountryRepository.countryFuture)
         when(MockCountryService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         when(MockCountryService.countryRepository.delete(anyLong)).thenReturn(Future.successful(1))
         Delete("/country/3") ~> testRoute ~> check {
            responseAs[String] shouldBe "Deleted country successfully".asJson
         }
      }
      "throw exception if country list is empty" in {
         when(MockCountryService.countryRepository.getAllCountry).thenReturn(MockCountryRepository.emptyList)
         when(MockCountryService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         Delete("/country/3") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Country list is empty!!".asJson
         }
      }
      "throw exception in delete country if foreign key relation exists" in {
         when(MockCountryService.countryRepository.getAllCountry).thenReturn(MockCountryRepository.countryFuture)
         when(MockCountryService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         Delete("/country/2") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Foreign key relation found in company table!!".asJson
         }
      }
      "throw exception for delete country for country id as 5" in {
         when(MockCountryService.countryRepository.getAllCountry).thenReturn(MockCountryRepository.countryFuture)
         when(MockCountryService.companyRepository.getAllCompany).thenReturn(MockCompanyRepository.companyFuture)
         Delete("/country/5") ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }
      }
      "update country details" in {
         val updatedCountry = Country(1, "Indonesia", language = "English", code = "INA").asJson
         when(MockCountryService.countryRepository.getAllCountry).thenReturn(MockCountryRepository.countryFuture)
         when(MockCountryService.countryRepository.update(anyLong, any[Country])).thenReturn(Future.successful(1))
         Put("/country/1").withEntity(updatedCountry) ~> testRoute ~> check {
            responseAs[String] shouldBe "Updated country successfully".asJson
         }
      }
      "throw exception in update if country list is empty" in {
         val updatedCountry = Country(1, "Indonesia", language = "English", code = "INA").asJson
         when(MockCountryService.countryRepository.getAllCountry).thenReturn(MockCountryRepository.emptyList)
         Put("/country/1").withEntity(updatedCountry) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Country list is empty!!".asJson
         }
      }
      "throw exception if unique code is violated " in {
         val updatedCountry = Country(1, "Indonesia", language = "English", code = "IND").asJson
         when(MockCountryService.countryRepository.getAllCountry).thenReturn(MockCountryRepository.countryFuture)
         Put("/country/1").withEntity(updatedCountry) ~> testRoute ~> check {
            status shouldEqual StatusCodes.Conflict
            responseAs[String] shouldBe "Updated country has duplicate code!!".asJson
         }
      }
      "throw exception if country for country id as 4" in {
         val updatedCountry = Country(4, "Indonesia", language = "English", code = "INA").asJson
         when(MockCountryService.countryRepository.getAllCountry).thenReturn(MockCountryRepository.countryFuture)
         Put("/country/4").withEntity(updatedCountry) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
            responseAs[String] shouldBe "Country for given id doesn't exists!!".asJson
         }
      }
      "throw exception for update country for name and language not defined" in {
         when(MockCountryService.countryRepository.getAllCountry).thenReturn(MockCountryRepository.countryFuture)
         val updatedCountry = Country(3, "", "", "").asJson
         Put("/country/3").withEntity(updatedCountry) ~> testRoute ~> check {
            status shouldEqual StatusCodes.BadRequest
         }

      }
   }

}
