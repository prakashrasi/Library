package com.reactore.feature

/**
  * created by Kartik on 13-11-2017
  */

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.reactore.core.CustomDirectives._
import com.reactore.core._
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class VehicleTypeRestTestSpec extends WordSpec with Matchers with ScalatestRouteTest {
   val vehicleTypeRest  = new VehicleTypeRest(MockVehicleTypeService)
   val testRoute: Route = vehicleTypeRest.vehicleTypeRoute
   "Vehicle Type Rest" should {
      "get all vehicle types" in {
         when(MockVehicleTypeService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(MockVehicleTypeRepository.vehicleTypeFuture)
         Get("/vehicletype") ~> testRoute ~> check {
            responseAs[String] shouldBe MockVehicleTypeRepository.vehicleTypeList.asJson
         }
      }
   }

}
