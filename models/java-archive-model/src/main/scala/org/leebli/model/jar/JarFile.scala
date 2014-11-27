package org.leebli.model.jar

import org.leebli.model.MavenInfo

case class JarFile(classes: List[JavaClass] = Nil,
  outgoingDependencies: List[JarFile] = Nil,
  incomingDependencies: List[JarFile] = Nil,
  mavenInfo: Option[MavenInfo] = None,
  name: String,
  size: Option[Long] = None,
  containsWebService: Boolean = false,
  containsRESTService: Boolean = false,
  containsJPAEntities: Boolean = false,
  id: Option[Long] = None) {

  def getAllContainedPackages = classes.map(_.packageName).distinct

  def jarFileName = if (mavenInfo.isDefined)
    mavenInfo.head.artifactId + "-" + mavenInfo.head.version
  else name

  def getAllExternallyReferencedPackages(): List[String] = {

    val externalImports =
      for {
        pack <- getAllContainedPackages
        if (pack.isDefined && !containsPackage(pack.head))
      } yield pack.head

    externalImports.distinct
  }

  def getAllUnidentifiableExternallyReferencedPackages(): List[String] = {
    val unresolvablePackages =
      for {
        pack <- getAllExternallyReferencedPackages
        dep <- outgoingDependencies
        val packageName = pack.asInstanceOf[String]
        val jar = dep.asInstanceOf[JarFile]
        if (!jar.containsPackage(packageName))
      } yield packageName

    unresolvablePackages.distinct
  }

  def getPackageCount = getAllContainedPackages.size

  def containsPackage(s: String): Boolean = {
    if (getAllContainedPackages == null) {
      return false
    }
    getAllContainedPackages.contains(s)
  }

  def nbClasses() = classes.size

  override def equals(obj: Any) = obj.isInstanceOf[JarFile] && this.name == obj.asInstanceOf[JarFile].name

  override def toString() = if (mavenInfo.isDefined) mavenInfo.toString else name

}