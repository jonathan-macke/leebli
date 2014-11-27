package org.leebli.parser.util

import grizzled.slf4j.Logger

object RegexUtil {

  val logger = Logger(getClass)

  val regexp = """(.*?)\/?([^\/]*?)-([rR]?[0-9].*).jar""".r

  def identifyFolderAndJarNameAndVersion(path: String): Option[(String, String, String)] =
    path match {
      case regexp(folder, name, version) => Some((folder, name, version))
      case _ => None
    }

}