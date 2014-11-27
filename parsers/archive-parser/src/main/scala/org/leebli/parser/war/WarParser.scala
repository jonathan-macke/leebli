package org.leebli.parser.war

import java.io.InputStream
import org.leebli.model.war.WarFile
import scala.collection.mutable.MutableList
import org.apache.commons.compress.archivers.ArchiveStreamFactory
import org.apache.commons.compress.archivers.ArchiveInputStream
import java.io.BufferedInputStream
import org.leebli.model.jar.JarFile
import org.leebli.model.MavenInfo
import org.leebli.parser.util.ZipInputStreamIterator._
import com.github.nscala_time.time.Imports._
import grizzled.slf4j.Logger
import org.leebli.parser.jar.JarParser
import java.util.Properties
import org.leebli.parser.util.RegexUtil

object WarParser {

  val logger = Logger(getClass)

  /**
   * parse a war file
   */
  def parseWarFile(name: String, input: InputStream, parseJavaClass: Boolean = false): WarFile = {

    def isArchive(f: String): Boolean =
      Seq(".zip", ".jar", ".war", ".ear") exists f.endsWith

    def isPom(f: String): Boolean = f.contains("META-INF/maven") && f.endsWith("pom.properties")

    def isJarFile(f: String): Boolean = f.endsWith(".jar")

    val archiveStream: Either[ArchiveInputStream, String] = {
      try {
        Left(new ArchiveStreamFactory().createArchiveInputStream(
          new BufferedInputStream(input)))
      } catch {
        case e: Exception => Right(s"Invalid archive : $e.getMessage")
      }
    }

    if (archiveStream.isRight) {
      WarFile(
        name = name,
        valid = false,
        errorMessage = archiveStream.right.get)
    } else {
      val jarFiles = MutableList[JarFile]()
      var mavenInfo: Option[MavenInfo] = None

      if (archiveStream.isLeft) {
        val zin = archiveStream.left.get
        for (child <- zin) {
          val n = child.getName
          val s = child.getSize
          logger.debug(s"$name!$n : $s")

          if (isJarFile(n)) {

            jarFiles += JarParser.parseJarFile(n, Some(s), zin, parseJavaClass)

          } else if (isPom(n)) {
            val properties = new Properties
            properties.load(zin)
            mavenInfo = Some(MavenInfo(
              properties.getProperty("groupId"), properties.getProperty("artifactId"), properties.getProperty("version")))
          } else {

          }
        }
      }

      if (mavenInfo.isEmpty) {
        //get artifactId and version from war name

        try {
          val Some((folder, artifactId, version)) =
            RegexUtil.identifyFolderAndJarNameAndVersion(name)

          mavenInfo = Some(MavenInfo(
            "not_identified", artifactId, version))
        } catch {
          case e: MatchError =>
            logger.warn(s"not able to identify mavenInfo in $name")
            mavenInfo = Some(MavenInfo(
              "not_identified", name, "not_identified"))
        }

      }

      WarFile(
        jarFiles = jarFiles.toList,
        name = name,
        mavenInfo = mavenInfo)
    }

  }

}