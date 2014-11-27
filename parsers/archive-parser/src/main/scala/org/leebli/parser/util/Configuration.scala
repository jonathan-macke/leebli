package org.leebli.parser.util

import java.util.ResourceBundle
import java.util.StringTokenizer
import scala.collection.mutable.MutableList

object Configuration {

  var ignorePackages: MutableList[String] = MutableList()

  var ignoreJars: MutableList[String] = MutableList()

  /*val filters = ResourceBundle.getBundle("com.kirkk.analyzer.framework.Filter")

  val packageFilters = filters.getString("filter.packages")

  val packageTokenizer = new StringTokenizer(packageFilters, ";")

  while (packageTokenizer.hasMoreTokens()) {
    val token = packageTokenizer.nextToken()
    ignorePackages += token.substring(0, token.length - 1)
  }

  val jarFilters = filters.getString("filter.jars")

  val jarTokenizer = new StringTokenizer(jarFilters, ";")

  while (jarTokenizer.hasMoreTokens()) {
    val token = jarTokenizer.nextToken()
    ignoreJars += token.substring(0, token.length)
  }*/

  def initialize(packageFilter: String, jarFilter: String) {
    if (packageFilter != null) {
      ignorePackages = new MutableList()
      val packageTokenizer = new StringTokenizer(packageFilter, ";")
      while (packageTokenizer.hasMoreTokens()) {
        val token = packageTokenizer.nextToken()
        ignorePackages += token.substring(0, token.length - 1)
      }
    }
    if (jarFilter != null) {
      ignoreJars = new MutableList()
      val jarTokenizer = new StringTokenizer(jarFilter, ";")
      while (jarTokenizer.hasMoreTokens()) {
        val token = jarTokenizer.nextToken()
        ignoreJars += token.substring(0, token.length)
      }
    }
  }
}