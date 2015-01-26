package org.leebli.model.jar

case class JavaClass(packageName: Option[String] = None,
  name: String,
  isAbstract: Boolean = false,
  isPublic: Boolean = true,
  isInterface: Boolean = false,
  importedPackages: List[String] = Nil,
  size: Option[Long] = None,
  id: Option[Long] = None,
  jarId: Option[Long] = None,
  annotations: List[Annotation] = Nil,
  calls: Map[String, Set[ClassMethod]] = Map.empty) {

  def getImportedPackageNames = this.importedPackages

  def flatCalls: Set[(ClassMethod, ClassMethod)] = for {
    (method, called) <- calls.toSet
    to <- called
  } yield ClassMethod(name, method) -> to

}

case class Annotation(annotationType: String) {
  override def toString() = annotationType
}

case class ClassMethod(className: String, method: String) {
  override def toString = s"$className.$method "
}