package com.reactore.feature

import com.reactore.core._
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

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
      "throw exception for get country by id for id as 4 " in {
         when(MockCountryService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         val result = MockCountryService.getCountryById(4)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception for get country by id for empty country list" in {
         when(MockCountryService.countryRepository.countryFuture).thenReturn(MockCountryRepository.emptyList)
         val result = MockCountryService.getCountryById(1)
         result.failed.futureValue shouldBe an[EmptyListException]
      }

      //test cases for insertCountry method
      "insert the country to list" in {
         when(MockCountryService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         when(MockCountryService.countryRepository.insert(any[Country])).thenReturn(Future.successful(1))
         val newCountry = Country(4, "FRANCE", "FRENCH", "FRA")
         val result = MockCountryService.insertCountry(newCountry)
         result.futureValue shouldBe "Inserted country successfully"
      }
      "throw exception in insert company for country name and language not defined" in {
         when(MockCountryService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         val newCountry = Country(4, "", "", "XXX")
         val result = MockCountryService.insertCountry(newCountry)
         result.failed.futureValue shouldBe an[FieldNotDefinedException]
      }
      "throw exception in insert country for duplicate country code" in {
         when(MockCountryService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         val newCountry = Country(4, "INDONESIA", "ENGLISH", "IND")
         val result = MockCountryService.insertCountry(newCountry)
         result.failed.futureValue shouldBe an[UniqueKeyViolationException]
      }

      //test cases for updateCountryById method
      "update country by id for country id as 3" in {
         when(MockCountryService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         when(MockCountryService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         when(MockCountryService.countryRepository.update(any[Long], any[Country])).thenReturn(Future.successful(1))
         val updatedCountry = Country(3, "Germany", "English", "GRM")
         val result = MockCountryService.updateCountryById(3, updatedCountry)
         result.futureValue shouldBe 1
      }
      "throw exception for update country for name and language not defined" in {
         when(MockCountryService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         val updatedCountry = Country(3, "", "", "GRM")
         val result = MockCountryService.updateCountryById(3, updatedCountry)
         result.failed.futureValue shouldBe an[FieldNotDefinedException]
      }
      "throw exception for update by id for country id as 5" in {
         when(MockCountryService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         val updatedCountry = Country(5, "Germany", "English", "GRM")
         val result = MockCountryService.updateCountryById(5, updatedCountry)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception for update country by id for empty country list" in {
         when(MockCountryService.countryRepository.countryFuture).thenReturn(MockCountryRepository.emptyList)
         val updatedCountry = Country(3, "Germany", "English", "GRM")
         val result = MockCountryService.updateCountryById(3, updatedCountry)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception for update country by id for duplicate country code" in {
         when(MockCountryService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         val updatedCountry = Country(1, "INDONESIA", "ENGLISH", "IND")
         val result = MockCountryService.updateCountryById(1, updatedCountry)
         result.failed.futureValue shouldBe an[UniqueKeyViolationException]
      }

      // test cases for deleteCountryById method
      "delete country in delete country by id for id as 3" in {
         when(MockCountryService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         when(MockCountryService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         when(MockCountryService.countryRepository.delete(any[Long])).thenReturn(Future.successful(1))
         val result = MockCountryService.deleteCountryById(3)
         result.futureValue shouldBe 1
      }
      "throw exception for delete country for empty country list" in {
         when(MockCountryService.countryRepository.countryFuture).thenReturn(MockCountryRepository.emptyList)
         when(MockCountryService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val result = MockCountryService.deleteCountryById(1)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception for delete country for country id as 5" in {
         when(MockCountryService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         when(MockCountryService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val result = MockCountryService.deleteCountryById(5)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception for delete country by id if foreign key relation found" in {
         when(MockCountryService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         when(MockCountryService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val result = MockCountryService.deleteCountryById(1)
         result.failed.futureValue shouldBe an[ForeignKeyRelationFoundException]
      }
   }
}

object MockCountryService extends CountryService with MockCountryFacade
