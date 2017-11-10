package com.reactore.generator

import com.reactore.core.DBProperties

/**
  * created by Kartik on 10-11-2017
  */

object CodeGenerator extends App with DBProperties {
   val canonicalPath = new java.io.File(".").getCanonicalPath
   val directoryPath = canonicalPath + "\\src\\main"
   val directoryName = "generated"
   slick.codegen.SourceCodeGenerator.main(Array(profile, driver, url, directoryPath, directoryName, userName, password))
}