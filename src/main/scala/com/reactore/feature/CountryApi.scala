package com.reactore.feature

import com.reactore.core._
import HandleExceptions._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * created by Kartik on 11-11-2017
  */
class CountryService {
   self: CountryFacadeComponent =>

   // save country
   def insertCountry(country: Country): Future[Int] = {
      for {
         countryList <- countryRepository.countryFuture
         res = if (country.name.nonEmpty && country.language.nonEmpty) {
            if (countryList.nonEmpty) {
               val uniqueCodeOption = countryList.find(_.code == country.code)
               if (uniqueCodeOption.isEmpty) {
                  countryRepository.insert(country)
               } else throw UniqueKeyViolationException(exception = new Exception("Unique country code violated!!"))
            } else countryRepository.insert(country)
         } else throw FieldNotDefinedException(exception = new Exception("Name/Language not defined!!"))
      } yield res
   }.flatten.recover { case ex => handleExceptions(ex) }

   //get country by id
   def getCountryById(id: Long): Future[Country] = {
      for {
         countryList <- countryRepository.countryFuture
         res = if (countryList.nonEmpty) {
            val countryOption = countryList.find(_.countryId == id)
            if (countryOption.isDefined) {
               countryOption.get
            } else throw NoSuchEntityException(exception = new Exception("Country not found!!"))
            //countryOption.getOrElse(throw NoSuchEntityException(exception = new Exception("Country not found!!")))
         } else throw EmptyListException(exception = new Exception("Country list is empty!!"))
      } yield res
   }.recover { case ex => handleExceptions(ex) }

   //delete country by id
   def deleteCountryById(id: Long): Future[Int] = {
      for {
         countryList <- countryRepository.countryFuture
         companyList <- companyRepository.companyFuture
         res = if (countryList.nonEmpty) {
            val countryOption = countryList.find(_.countryId == id)
            if (countryOption.isDefined) {
               val companiesForGivenCountry = companyList.filter(_.country == id)
               if (companiesForGivenCountry.isEmpty) {
                  countryRepository.delete(id)
               } else throw ForeignKeyRelationFoundException(exception = new Exception("Foreign key relation found in company table!!"))
            } else throw NoSuchEntityException(exception = new Exception("Country for given id doesn't exists!!"))
         } else throw EmptyListException(exception = new Exception("Country list is empty!!"))
      } yield res
   }.flatten.recover { case ex => handleExceptions(ex) }

   // update country by id
   def updateCountryById(id: Long, updatedCountry: Country): Future[Int] = {
      for {
         countryList <- countryRepository.countryFuture
         res = if (updatedCountry.name.nonEmpty && updatedCountry.language.nonEmpty && updatedCountry.code.nonEmpty) {
            if (countryList.nonEmpty) {
               val countryOption = countryList.find(_.countryId == id)
               if (countryOption.isDefined) {
                  val uniqueCountryCode = countryList.find(_.code.equalsIgnoreCase(updatedCountry.code))
                  if (uniqueCountryCode.isEmpty) {
                     countryRepository.update(id,updatedCountry)
                  } else throw UniqueKeyViolationException(exception = new Exception("Updated country has duplicate code!!"))
               } else throw NoSuchEntityException(exception = new Exception("Country for given id doesn't exists!!"))
            } else throw EmptyListException(exception = new Exception("Country list is empty!!"))
         } else throw FieldNotDefinedException(exception = new Exception("Fields are not defined!!"))
      } yield res
   }.flatten.recover { case ex => handleExceptions(ex) }

}

class CountryRest {

}