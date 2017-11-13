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
      for {
         countryList <- countryRepository.countryFuture
         companyList <- companyRepository.companyFuture
         res = if (company.name.nonEmpty && company.licenceNumber.nonEmpty) {
            if (countryList.nonEmpty) {
               val validCountry = countryList.find(_.countryId == company.country)
               if (validCountry.isDefined) {
                  if (companyList.nonEmpty) {
                     val uniqueCountry = companyList.find(comp => comp.name.toLowerCase == company.name.toLowerCase && comp.licenceNumber.toLowerCase == company.licenceNumber.toLowerCase)
                     if (uniqueCountry.isEmpty) {
                        companyRepository.insert(company)
                        Future.successful("Inserted company successfully!!")

                     } else throw DuplicateEntityException(message = "Company already defined!!", exception = new Exception("Company already defined!!"))
                  } else {
                     companyRepository.insert(company)
                     Future.successful("Inserted company successfully!!")
                  }
               } else throw NoSuchEntityException(message = "Country does not exists!!", exception = new Exception("Country does not exists!!"))
            } else throw EmptyListException(message = "Country list is Empty!!", exception = new Exception("Country list is Empty!!"))
         } else throw FieldNotDefinedException(message = "Fields are not defined!!", exception = new Exception("Fields are not defined!!"))
      } yield res
   }.flatten.recover { case ex => handleExceptions(ex) }

   //get company by id
   def getCompanyById(id: Long): Future[Company] = {
      for {
         companyList <- companyRepository.companyFuture
         res = if (companyList.nonEmpty) {
            val companyOption = companyList.find(_.companyId == id)
            if (companyOption.isDefined) {
               companyOption.get
            } else throw NoSuchEntityException(message = "Company not found!!", exception = new Exception("Company not found!!"))
         } else throw EmptyListException(message = "Company list is empty!!", exception = new Exception("Company list is empty!!"))
      } yield res
   }.recover { case ex => handleExceptions(ex) }

   //delete company by id
   def deleteCompanyById(id: Long): Future[String] = {
      for {
         companyList <- companyRepository.companyFuture
         vehicleList <- vehicleRepository.vehiclesFuture
         res = if (companyList.nonEmpty) {
            val companyOption = companyList.find(_.companyId == id)
            if (companyOption.isDefined) {
               val vehiclesForGivenCompany = vehicleList.filter(_.company == id)
               if (vehiclesForGivenCompany.isEmpty) {
                  companyRepository.delete(id)
                  Future.successful("Deleted company successfully")
               } else throw ForeignKeyRelationFoundException(message = "Foreign key relation found in vehicle table!!", exception = new Exception("Foreign key relation found in vehicle table!!"))
            } else throw NoSuchEntityException(message = "Company not found!!", exception = new Exception("Company not found!!"))
         } else throw EmptyListException(message = "Company list is empty!!", exception = new Exception("Company list is empty!!"))
      } yield res
   }.flatten.recover { case ex => handleExceptions(ex) }

   //update company by id
   def updateCompanyById(id: Long, updatedCompany: Company): Future[String] = {
      for {
         companyList <- companyRepository.companyFuture
         countryList <- countryRepository.countryFuture
         res = if (updatedCompany.name.nonEmpty && updatedCompany.licenceNumber.nonEmpty) {
            val companyOption = companyList.find(_.companyId == id)
            if (companyOption.isDefined) {
               val validCountry = countryList.find(_.countryId == updatedCompany.country)
               if (validCountry.isDefined) {
                  val uniqueCompany = companyList.find(company => company.name.equalsIgnoreCase(updatedCompany.name) && company.licenceNumber.equalsIgnoreCase(updatedCompany.licenceNumber) && company.country == updatedCompany.country)
                  if (uniqueCompany.isEmpty) {
                     companyRepository.update(id, updatedCompany)
                     Future.successful("Updated company successfully")
                  } else throw DuplicateEntityException(message = "Updated company is already present!!", exception = new Exception("Updated company is already present!!"))
               } else throw NoSuchEntityException(exception = new Exception("No country found!!"), message = "No country found!!")
            } else throw NoSuchEntityException(exception = new Exception("No company found for given id!!"), message = "No company found for given id!!")
         } else throw FieldNotDefinedException(exception = new Exception("Fields not defined!!"), message = "Fields not defined!!")
      } yield res
   }.flatten.recover { case ex => handleExceptions(ex) }

   def getAll: Future[Seq[Company]] = {
      for {
         companies <- companyRepository.companyFuture
         res = if (companies.nonEmpty) {companies} else throw EmptyListException(message = "Company list is empty!!", exception = new Exception("Company list is empty!!"))
      } yield res
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