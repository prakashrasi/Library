package com.reactore.feature

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * created by Kartik on 13-11-2017
  */
object ApiBoot extends App with Directives {
   implicit val system      : ActorSystem       = ActorSystem("My-System")
   implicit val materializer: ActorMaterializer = ActorMaterializer()

   val vehicleRest         = new VehicleRest(ImplVehicleService)
   val countryRest         = new CountryRest(ImplCountryService)
   val vehicleCategoryRest = new VehicleCategoryRest(ImplVehicleCategoryService)
   val vehicleTypeRest     = new VehicleTypeRest(ImplVehicleTypeService)
   val companyRest         = new CompanyRest(ImplCompanyService)
   val finalRoute          = pathPrefix("bigapp") {
      vehicleRest.vehicleRoute ~ countryRest.countryRoute ~ vehicleCategoryRest.vehicleCategoryRoute ~ vehicleTypeRest.vehicleTypeRoute ~ companyRest.companyRoute
   }
   val binding             = Http().bindAndHandle(finalRoute, "localhost", 8085)
   binding.onComplete {
      case Success(res) => println("Bind Success>>>" + res)
      case Failure(ex)  => println("Bind Failed>>"); ex.printStackTrace()
   }
}
