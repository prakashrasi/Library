package com.reactore.generator

/**
  * created by Kartik on 10-11-2017
  */

import com.reactore.core.DBProperties

object CodeGenerator extends App with DBProperties {
   val canonicalPath = new java.io.File(".").getCanonicalPath
   val directoryPath = canonicalPath + "\\src\\main"
   val directoryName = "generated"
   slick.codegen.SourceCodeGenerator.main(Array(profile, driver, url, directoryPath, directoryName, userName, password))
}