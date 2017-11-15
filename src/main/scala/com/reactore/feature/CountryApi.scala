package com.reactore.feature

/**
  * created by Kartik on 11-11-2017
  */

import org.json4s.native.Serialization._
import com.reactore.core._
import HandleExceptions._
import akka.http.scaladsl.server.Route

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class CountryService {
   self: CountryFacadeComponent =>

   // save country
   def insertCountry(country: Country): Future[String] = {
      val result = for {
         countryList <- countryRepository.countryFuture
         _ = if (country.name.isEmpty && country.language.isEmpty) throw FieldNotDefinedException(exception = new Exception("Fields not defined!!"), message = "Fields not defined!!")
         _ = if (countryList.isEmpty) countryRepository.insert(country).map { x => "Inserted country successfully" }
         uniqueCodeOption = countryList.find(_.code == country.code)
         res <- if (uniqueCodeOption.isEmpty) {
            countryRepository.insert(country).map { x => "Inserted country successfully" }
         } else Future.failed(UniqueKeyViolationException(exception = new Exception("Unique country code violated!!"), message = "Unique country code violated!!"))
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   //get country by id
   def getCountryById(id: Long): Future[Country] = {
      val result = for {
         countryList <- countryRepository.countryFuture
         _ = if (countryList.isEmpty) throw EmptyListException(exception = new Exception("Country list is empty!!"), message = "Country list is empty!!")
         countryOption = countryList.find(_.countryId == id)
         res = if (countryOption.isDefined) {
            countryOption.get
         } else throw NoSuchEntityException(exception = new Exception("Country not found!!"), message = "Country not found!!")
         //countryOption.getOrElse(throw NoSuchEntityException(exception = new Exception("Country not found!!")))
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   //delete country by id
   def deleteCountryById(id: Long): Future[String] = {
      val result = for {
         countryList <- countryRepository.countryFuture
         companyList <- companyRepository.companyFuture
         _ = if (countryList.isEmpty) throw EmptyListException(exception = new Exception("Country list is empty!!"), message = "Country list is empty!!")
         countryOption = countryList.find(_.countryId == id)
         _ = if (countryOption.isEmpty) throw NoSuchEntityException(exception = new Exception("Country for given id doesn't exists!!"), message = "Country for given id doesn't exists!!")
         companiesForGivenCountry = companyList.filter(_.country == id)
         _ = if (companiesForGivenCountry.nonEmpty) throw ForeignKeyRelationFoundException(exception = new Exception("Foreign key relation found in company table!!"), message = "Foreign key relation found in company table!!")
         res <- countryRepository.delete(id).map { x => "Deleted country successfully" }
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   // update country by id
   def updateCountryById(id: Long, updatedCountry: Country): Future[String] = {
      val result = for {
         countryList <- countryRepository.countryFuture
         _ = if (updatedCountry.name.isEmpty && updatedCountry.language.isEmpty && updatedCountry.code.isEmpty) throw FieldNotDefinedException(exception = new Exception("Fields are not defined!!"), message = "Fields are not defined!!")
         _ = if (countryList.isEmpty) throw EmptyListException(exception = new Exception("Country list is empty!!"), message = "Country list is empty!!")
         countryOption = countryList.find(_.countryId == id)
         _ = if (countryOption.isEmpty) throw NoSuchEntityException(exception = new Exception("Country for given id doesn't exists!!"), message = "Country for given id doesn't exists!!")
         uniqueCountryCode = countryList.find(_.code.equalsIgnoreCase(updatedCountry.code))
         res <- if (uniqueCountryCode.isEmpty) {
            countryRepository.update(id, updatedCountry).map { x => "Updated country successfully" }
         } else Future.failed(UniqueKeyViolationException(exception = new Exception("Updated country has duplicate code!!"), message = "Updated country has duplicate code!!"))
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

   def getAll: Future[Seq[Country]] = {
      val result = for {
         countryList <- countryRepository.countryFuture
         _ = if (countryList.isEmpty) throw EmptyListException(message = "Country list is empty!!", exception = new Exception("Country list is empty!!"))
         res = countryList
      } yield res
      result.recover { case ex => handleExceptions(ex) }
   }

}

object ImplCountryService extends CountryService with CountryFacade

class CountryRest(countryService: CountryService) extends CustomDirectives {
   val countryRoute: Route = path("country") {
      pathEndOrSingleSlash {
         get {
            val result = countryService.getAll
            complete(respond(result))
         } ~ post {
            entity(as[String]) {
               country =>
                  val newCountry = read[Country](country)
                  val result = countryService.insertCountry(newCountry)
                  complete(respond(result))
            }
         }
      }
   } ~ path("country" / LongNumber) {
      id =>
         get {
            val result = countryService.getCountryById(id)
            complete(respond(result))
         } ~ put {
            entity(as[String]) {
               country =>
                  val updateCountry = read[Country](country)
                  val result = countryService.updateCountryById(id, updateCountry)
                  complete(respond(result))
            }
         } ~ delete {
            val result = countryService.deleteCountryById(id)
            complete(respond(result))
         }
   }
}
