package com.reactore.feature

import org.scalatest.{Matchers, WordSpec}
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures

/**
  * created by Kartik on 11-11-2017
  */
class CountryServiceTestSpec extends WordSpec with ScalaFutures with Matchers {
   "Country Service " should {
      //test cases for getCountryById
      "return a country in get by id for id as 1" in {
         when(MockCountryService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         val expectedResult = MockCountryRepository.country1
         val result = MockCountryService.getCountryById(1)
         result.futureValue shouldBe expectedResult

      }
   }
}

object MockCountryService extends CountryService with MockCountryFacade
