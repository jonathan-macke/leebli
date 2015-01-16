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

  def nbOfJarFiles() = jarFiles.size
  
  def nbOfEjbModules() = jarFiles.filter(_.ejbModule).size
  
  def allClasses() = jarFiles flatMap (_.classes)
  
  def allPackages() = allClasses map (_.packageName.get) distinct
  
  override def toString() = if (mavenInfo.isDefined) mavenInfo.toString else name
}