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
         when(MockCountryService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         Get("/country") ~> testRoute ~> check {
            responseAs[String] shouldBe MockCountryRepository.countryList.asJson
         }
      }
      "throw exception if country list is empty" in {
         when(MockCountryService.countryRepository.countryFuture).thenReturn(MockCountryRepository.emptyList)
         Get("/country") ~> testRoute ~> check {
            responseAs[String] shouldBe "Country list is empty!!".asJson
         }
      }
      "insert country to list" in {
         val newCountry = Country(4, "FRANCE", language = "FRENCH", code = "FRA").asJson
         when(MockCountryService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         when(MockCountryService.countryRepository.insert(any[Country])).thenReturn(Future.successful(1))
         Post("/company").withEntity(newCountry)~>testRoute~>check{
            responseAs[String] shouldBe ""
         }

      }
   }

}
