package com.reactore.feature

/**
  * created by Kartik on 12-11-2017
  */

import java.sql.Timestamp

import com.reactore.core._
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class CompanyServiceTestSpec extends WordSpec with Matchers with ScalaFutures {
   "Company Service " should {
      // test cases for getAll method
      "return all company list " in {
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val result = MockCompanyService.getAll
         val expectedResult = MockCompanyRepository.companyList
         result.futureValue shouldBe expectedResult
      }
      "throw exception in get all for empty company list" in {
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.emptyList)
         val result = MockCompanyService.getAll
         result.failed.futureValue shouldBe an[EmptyListException]
      }

      // test cases for getCompanyById method
      "return a company in get company for company id as 1" in {
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val expectedResult = MockCompanyRepository.company1
         val result = MockCompanyService.getCompanyById(1)
         result.futureValue shouldBe expectedResult
      }
      "throw exception in get company for empty company list" in {
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.emptyList)
         val result = MockCompanyService.getCompanyById(2)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in get company by id for company id 5" in {
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val result = MockCompanyService.getCompanyById(5)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }

      // test cases for insertCompany method
      "insert company  to list in insert company " in {
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         when(MockCompanyService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         when(MockCompanyService.companyRepository.insert(any[Company])).thenReturn(Future.successful(1))
         val newCompany = Company(4, "IVECO", licenceNumber = "IVEC009", country = 3,startYear = Timestamp.valueOf("1970-01-01 00:00:00"))
         val result = MockCompanyService.insertCompany(newCompany)
         result.futureValue shouldBe "Inserted company successfully!!"
      }
      "throw exception in insert company if name and licence number not defined" in {
         when(MockCompanyService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val newCompany = Company(4, "", licenceNumber = "", country = 3,startYear = Timestamp.valueOf("1970-01-01 00:00:00"))
         val result = MockCompanyService.insertCompany(newCompany)
         result.failed.futureValue shouldBe an[FieldNotDefinedException]
      }
      "throw exception in insert company if country does not exists" in {
         when(MockCompanyService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val newCompany = Company(4, "RENAULT", licenceNumber = "REN001", country = 4,startYear = Timestamp.valueOf("1970-01-01 00:00:00"))
         val result = MockCompanyService.insertCompany(newCompany)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception for insert company if country list is empty " in {
         when(MockCompanyService.countryRepository.countryFuture).thenReturn(MockCountryRepository.emptyList)
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val newCompany = Company(4, "BENZ", licenceNumber = "BEN009", country = 3,startYear = Timestamp.valueOf("1970-01-01 00:00:00"))
         val result = MockCompanyService.insertCompany(newCompany)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception for insert company if duplicate country is inserted" in {
         when(MockCompanyService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val newCompany = Company(1, "TATA", licenceNumber = "TA001", country = 3,startYear = Timestamp.valueOf("1970-01-01 00:00:00"))
         val result = MockCompanyService.insertCompany(newCompany)
         result.failed.futureValue shouldBe an[DuplicateEntityException]
      }

      //test cases for deleteCompanyById method
      "delete company in delete company for company id as 3" in {
         when(MockCompanyService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         when(MockCompanyService.companyRepository.delete(any[Long])).thenReturn(Future.successful(1))
         val result = MockCompanyService.deleteCompanyById(3)
         result.futureValue shouldBe "Deleted company successfully"
      }
      "throw exception in delete company for empty company list" in {
         when(MockCompanyService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.emptyList)
         val result = MockCompanyService.deleteCompanyById(1)
         result.failed.futureValue shouldBe an[EmptyListException]
      }
      "throw exception in delete company for company id as 5" in {
         when(MockCompanyService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val result = MockCompanyService.deleteCompanyById(5)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in delete company if foreign key relation exists" in {
         when(MockCompanyService.vehicleRepository.vehiclesFuture).thenReturn(MockVehicleRepository.vehicleFuture)
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         val result = MockCompanyService.deleteCompanyById(1)
         result.failed.futureValue shouldBe an[ForeignKeyRelationFoundException]
      }

      //test cases for updateCompanyById method
      "update company by id for company id as 1" in {
         when(MockCompanyService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         when(MockCompanyService.companyRepository.update(any[Long], any[Company])).thenReturn(Future.successful(1))
         val updatedCompany = Company(1, "Benz", licenceNumber = "BEN001", country = 1,startYear = Timestamp.valueOf("1970-01-01 00:00:00"))
         val result = MockCompanyService.updateCompanyById(1, updatedCompany)
         result.futureValue shouldBe "Updated company successfully"
      }
      "throw exception in update company if name and licence number not defined" in {
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         when(MockCompanyService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         val updatedCompany = Company(1, "", licenceNumber = "", country = 1,startYear = Timestamp.valueOf("1970-01-01 00:00:00"))
         val result = MockCompanyService.updateCompanyById(1, updatedCompany)
         result.failed.futureValue shouldBe an[FieldNotDefinedException]
      }
      "throw exception in update company for company id as 5" in {
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         when(MockCompanyService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         val updatedCompany = Company(5, "Benz", licenceNumber = "Ben001", country = 1,startYear = Timestamp.valueOf("1970-01-01 00:00:00"))
         val result = MockCompanyService.updateCompanyById(5, updatedCompany)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in update company if country does not exists" in {
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         when(MockCompanyService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         val updatedCompany = Company(1, "Benz", licenceNumber = "Ben001", country = 5,startYear = Timestamp.valueOf("1970-01-01 00:00:00"))
         val result = MockCompanyService.updateCompanyById(1, updatedCompany)
         result.failed.futureValue shouldBe an[NoSuchEntityException]
      }
      "throw exception in update company for duplicate company details" in {
         when(MockCompanyService.companyRepository.companyFuture).thenReturn(MockCompanyRepository.companyFuture)
         when(MockCompanyService.countryRepository.countryFuture).thenReturn(MockCountryRepository.countryFuture)
         val updatedCompany = Company(1, "TATA", licenceNumber = "TA001", country = 1,startYear = Timestamp.valueOf("1970-01-01 00:00:00"))
         val result = MockCompanyService.updateCompanyById(1, updatedCompany)
         result.failed.futureValue shouldBe an[DuplicateEntityException]
      }
   }
}

object MockCompanyService extends CompanyService with MockCompanyFacade