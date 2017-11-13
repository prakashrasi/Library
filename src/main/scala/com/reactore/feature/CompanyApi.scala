package com.reactore.feature

/**
  * created by Kartik on 10-11-2017
  */

import akka.http.scaladsl.server.Route
import org.json4s.native.Serialization._
import com.reactore.core.HandleExceptions._
import com.reactore.core._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CompanyService {
   self: CompanyFacadeComponent =>

   def insertCompany(company: Company): Future[String] = {
      val result = for {
         countryList <- countryRepository.countryFuture
         companyList <- companyRepository.companyFuture
         _ = if (company.name.isEmpty && company.licenceNumber.isEmpty) throw FieldNotDefinedException(message = "Fields are not defined!!", exception = new Exception("Fields are not defined!!"))
         _ = if (countryList.isEmpty) throw EmptyListException(message = "Country list is Empty!!", exception = new Exception("Country list is Empty!!"))
         validCountry = countryList.find(_.countryId == company.country)
         _ = if (validCountry.isEmpty) throw NoSuchEntityException(message = "Country does not exists!!", exception = new Exception("Country does not exists!!"))
         _ = if (companyList.isEmpty) companyRepository.insert(company).map { x => "Inserted company successfully!!" }
         uniqueCountry = companyList.find(comp => comp.name.toLowerCase == company.name.toLowerCase && comp.licenceNumber.toLowerCase == company.licenceNumber.toLowerCase)
         res <- if (uniqueCountry.isEmpty) {
            companyRepository.insert(company).map { x => "Inserted company successfully!!" }
         } else Future.failed(DuplicateEntityException(message = "Company already defined!!", exception = new Exception("Company already defined!!")))
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   //get company by id
   def getCompanyById(id: Long): Future[Company] = {
      val result = for {
         companyList <- companyRepository.companyFuture
         _ = if (companyList.isEmpty) throw EmptyListException(message = "Company list is empty!!", exception = new Exception("Company list is empty!!"))
         companyOption = companyList.find(_.companyId == id)
         res = if (companyOption.isDefined) {
            companyOption.get
         } else throw NoSuchEntityException(message = "Company not found!!", exception = new Exception("Company not found!!"))
      } yield res
      result.recover {
         case ex => handleExceptions(ex)
      }
   }

   //delete company by id
   def deleteCompanyById(id: Long): Future[String] = {
      val result = for {
         companyList <- companyRepository.companyFuture
         vehicleList <- vehicleRepository.vehiclesFuture
         _ = if (companyList.isEmpty) throw EmptyListException(message = "Company list is empty!!", exception = new Exception("Company list is empty!!"))
         companyOption = companyList.find(_.companyId == id)
         _ = if (companyOption.isEmpty) throw NoSuchEntityException(message = "Company not found!!", exception = new Exception("Company not found!!"))
         vehiclesForGivenCompany = vehicleList.filter(_.company == id)
         res <- if (vehiclesForGivenCompany.isEmpty) {
            companyRepository.delete(id).map {
               x => "Deleted company successfully"
            }
         } else Future.failed(ForeignKeyRelationFoundException(message = "Foreign key relation found in vehicle table!!", exception = new Exception("Foreign key relation found in vehicle table!!")))
      } yield res
      result.recover {
         case ex => handleExceptions(ex)
      }
   }

   //update company by id
   def updateCompanyById(id: Long, updatedCompany: Company): Future[String] = {
      val result = for {
         companyList <- companyRepository.companyFuture
         countryList <- countryRepository.countryFuture
         _ = if (updatedCompany.name.isEmpty && updatedCompany.licenceNumber.isEmpty) throw FieldNotDefinedException(exception = new Exception("Fields not defined!!"), message = "Fields not defined!!")
         companyOption = companyList.find(_.companyId == id)
         _ = if (companyOption.isEmpty) throw NoSuchEntityException(exception = new Exception("No company found for given id!!"), message = "No company found for given id!!")
         validCountry = countryList.find(_.countryId == updatedCompany.country)
         _ = if (validCountry.isEmpty) throw NoSuchEntityException(exception = new Exception("No country found!!"), message = "No country found!!")
         uniqueCompany = companyList.find(company => company.name.equalsIgnoreCase(updatedCompany.name) && company.licenceNumber.equalsIgnoreCase(updatedCompany.licenceNumber) && company.country == updatedCompany.country)
         res <- if (uniqueCompany.isEmpty) {
            companyRepository.update(id, updatedCompany).map { x => "Updated company successfully" }
         } else Future.failed(DuplicateEntityException(message = "Updated company is already present!!", exception = new Exception("Updated company is already present!!")))
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   def getAll: Future[Seq[Company]] = {
      val result = for {
         companies <- companyRepository.companyFuture
         _ = if (companies.isEmpty) throw EmptyListException(message = "Company list is empty!!", exception = new Exception("Company list is empty!!"))
         res = companies
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }
}

object ImplCompanyService extends CompanyService with CompanyFacade

class CompanyRest(companyService: CompanyService) extends CustomDirectives {

   val companyRoute: Route = path("company") {
      pathEndOrSingleSlash {
         get {
            val result = companyService.getAll
            complete(respond(result))
         } ~ post {
            entity(as[String]) {
               company =>
                  val newCompany = read[Company](company)
                  val result = companyService.insertCompany(newCompany)
                  complete(respond(result))
            }
         }
      }
   } ~ path("company" / LongNumber) {
      id =>
         get {
            val result = companyService.getCompanyById(id)
            complete(respond(result))
         } ~ put {
            entity(as[String]) {
               company =>
                  val updateCompany = read[Company](company)
                  val result = companyService.updateCompanyById(id, updateCompany)
                  complete(respond(result))
            }
         } ~ delete {
            val result = companyService.deleteCompanyById(id)
            complete(respond(result))
         }
   }
}