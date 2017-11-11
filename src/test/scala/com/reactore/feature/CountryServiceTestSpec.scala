package com.reactore.feature

import org.scalatest.{Matchers, WordSpec}
import org.scalatest.concurrent.ScalaFutures

/**
  * created by Kartik on 11-11-2017
  */
class CountryServiceTestSpec extends WordSpec with ScalaFutures with Matchers {

}

object MockCompanyService extends CompanyService with MockCompanyFacade
