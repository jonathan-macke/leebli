package org.leebli.parser.jar

import org.apache.commons.compress.archivers.ArchiveInputStream
import org.apache.commons.compress.archivers.ArchiveStreamFactory
import java.io.BufferedInputStream
import java.io.InputStream
import org.leebli.parser.util.ZipInputStreamIterator._
import com.github.nscala_time.time.Imports._
import java.util.Properties
import grizzled.slf4j.Logger
import org.leebli.model.jar.JavaClass
import org.leebli.model.jar.JarFile
import scala.collection.mutable.MutableList
import org.leebli.model.MavenInfo
import org.apache.bcel.classfile.ClassParser
import org.leebli.parser.util._
import org.apache.bcel.classfile.DescendingVisitor
import org.leebli.parser.util.Configuration
import org.leebli.parser.util.RegexUtil
import org.leebli.model.jar.Annotation
import org.apache.bcel.util.BCELifier

object JarParser {

  val logger = Logger(getClass)

  /**
   * parse a jar file
   */
  def parseJarFile(name: String, size: Option[Long] = None, input: InputStream, parseJavaClass: Boolean = false): JarFile = {

    def isJavaClass(f: String): Boolean = f.endsWith(".class")

    def isManifest(f: String): Boolean = f.endsWith("META-INF/MANIFEST.MF")

    def isPom(f: String): Boolean = f.contains("META-INF/maven") && f.endsWith("pom.properties")

    def isEjbModule(f: String): Boolean = f.contains("META-INF/ejb-jar.xml")

    val archiveStream: Either[ArchiveInputStream, String] = {
      try {
        Left(new ArchiveStreamFactory().createArchiveInputStream(
          new BufferedInputStream(input)))
      } catch {
        case e: Exception => Right(s"Invalid archive : $e.getMessage")
      }
    }

    var mavenInfo: Option[MavenInfo] = None
    var isEJbModule = false
    val javaClasses = MutableList[JavaClass]()

    if (archiveStream.isLeft) {
      val zin = archiveStream.left.get
      for (child <- zin) {
        val n = child.getName
        val s = child.getSize
        logger.debug(s"$name!$n : $s")

        if (isJavaClass(n)) {

          if (parseJavaClass) {
            val classParser = new ClassParser(zin, name);
            val javaClass = classParser.parse();

            val pVisitor = new PackageVisitor(javaClass);
            val dVisitor = new DescendingVisitor(javaClass, pVisitor);
            javaClass.accept(dVisitor);
            val analyser = new ClassAnalyser
            javaClass.accept(new DescendingVisitor(javaClass, analyser))
            val imports = pVisitor.getImports(Configuration.ignorePackages.toList);

            val annotations = for {
              annots <- javaClass.getAnnotationEntries.toList
              val annotationType = annots.getAnnotationType
            } yield Annotation(annotationType)

            javaClasses += JavaClass(
              packageName = Some(javaClass.getPackageName),
              name = javaClass.getClassName,
              size = Some(s),
              isAbstract = javaClass.isAbstract,
              isPublic = javaClass.isPublic,
              isInterface = javaClass.isInterface,
              importedPackages = imports,
              annotations = annotations,
              calls = analyser.calledMethods,
              parents = analyser.parents)

          } else {
            javaClasses += JavaClass(name = n, size = Some(s))
          }

        } else if (isPom(n)) {
          val properties = new Properties
          properties.load(zin)
          mavenInfo = Some(MavenInfo(
            properties.getProperty("groupId"), properties.getProperty("artifactId"), properties.getProperty("version")))
        } else if (isEjbModule(n)) {
          isEJbModule = true
        }
      }
    }

    if (mavenInfo.isEmpty) {
      //get artifactId and version from jar name

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

    JarFile(
      classes = javaClasses.toList,
      mavenInfo = mavenInfo,
      name = name,
      ejbModule = isEJbModule,
      size = size)

  }

}