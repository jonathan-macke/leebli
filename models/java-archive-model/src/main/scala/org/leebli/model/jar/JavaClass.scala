package org.leebli.model.jar

case class JavaClass(packageName: Option[String] = None,
  name: String,
  isAbstract: Boolean = false,
  isPublic: Boolean = true,
  isInterface: Boolean = false,
  importedPackages: List[String] = Nil,
  size: Option[Long] = None,
  id: Option[Long] = None,
  jarId: Option[Long] = None) {

  def getImportedPackageNames = this.importedPackages

  
}