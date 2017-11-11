package com.reactore.feature

import com.reactore.core.HandleExceptions._
import com.reactore.core._

import scala.concurrent.Future

/**
  * created by Kartik on 11-11-2017
  */
class VehicleService {
   self: VehicleFacadeComponent =>

   //save vehicle
   def insertVehicle(vehicle: Vehicle): Future[Int] = {
      for {
         vehicleList <- vehicleRepository.vehiclesFuture
         vehicleTypeList <- vehicleTypeRepository.vehicleTypeFuture
         companyList <- companyRepository.companyFuture
         res = if (vehicle.name.nonEmpty && vehicle.modelNumber.nonEmpty) {
            if (companyList.nonEmpty) {
               val companyOption = companyList.find(_.companyId == vehicle.company)
               if (companyOption.isDefined) {
                  if (vehicleTypeList.nonEmpty) {
                     val vehicleTypeOption = vehicleTypeList.find(_.vehicleTypeId == vehicle.vehicleType)
                     if (vehicleTypeOption.isDefined) {
                        if (vehicleList.nonEmpty) {
                           val uniqueModelNumber = vehicleList.find(_.modelNumber.equalsIgnoreCase(vehicle.modelNumber))
                           if (uniqueModelNumber.isEmpty) {
                              vehicleRepository.insert(vehicle)
                           } else throw UniqueKeyViolationException(exception = new Exception("Unique model number violated!!"))
                        } else vehicleRepository.insert(vehicle)
                     } else throw NoSuchEntityException(exception = new Exception("Vehicle type not found!!"))
                  } else throw EmptyListException(exception = new Exception("Vehicle type list is empty!!"))
               } else throw NoSuchEntityException(exception = new Exception("Company not found!!"))
            } else throw EmptyListException(exception = new Exception("Company list is empty!!"))
         } else throw FieldNotDefinedException(exception = new Exception("Name/Model number is not defined!!"))
      } yield res
   }.flatten.recover { case ex => handleExceptions(ex) }
}