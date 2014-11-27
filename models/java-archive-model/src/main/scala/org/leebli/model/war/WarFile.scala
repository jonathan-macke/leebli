package org.leebli.model.war

import org.leebli.model.jar.JarFile
import org.leebli.model.MavenInfo

case class WarFile(
  jarFiles: List[JarFile] = Nil,
  mavenInfo: Option[MavenInfo] = None,
  name: String,
  valid: Boolean = true,
  errorMessage: String = "",
  id: Option[Long] = None) {

  def warFileName = if (mavenInfo.isDefined)
    mavenInfo.head.artifactId + "-" + mavenInfo.head.version
  else
    name

  def isInvalid() = !valid

  override def toString() = if (mavenInfo.isDefined) mavenInfo.toString else name
}