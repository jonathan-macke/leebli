package org.leebli.parser.util

import java.util.Iterator
import java.util.StringTokenizer
import org.apache.bcel.classfile._
import scala.collection.JavaConversions._
import scala.collection.mutable.MutableList

class PackageVisitor(private var javaClass: JavaClass) extends EmptyVisitor {

  private var imports: MutableList[String] = MutableList()

  private var strings: MutableList[String] = MutableList()

  override def visitConstantClass(cls: ConstantClass) {
    var sCls = cls.getBytes(this.javaClass.getConstantPool)
    if (sCls.indexOf("/") != -1) {
      sCls = this.stripClassName(sCls)
      sCls = sCls.replace('/', '.')
      sCls = this.cleanClass(sCls)
      if (!this.imports.contains(sCls) && (this.javaClass.getPackageName != sCls)) {
        imports += sCls
      }
    }
  }

  override def visitConstantUtf8(utf: ConstantUtf8) {
    val utfString = utf.toString.substring(utf.toString.indexOf('"') + 1, utf.toString.lastIndexOf('"'))
    if (isValidJavaClass(utfString)) {
      if (!this.strings.contains(utfString)) {
        val classes = this.separateClasses(utfString)
        for (i <- 0 until classes.length if classes(i) != null) {
          val cls = classes(i)
          var packageName = this.stripClassName(cls)
          packageName = packageName.replace('/', '.')
          val cleanedPackage = this.cleanClass(packageName)
          if (!this.imports.contains(cleanedPackage) && (this.javaClass.getPackageName != cleanedPackage)) {
            imports += cleanedPackage
          }
        }
      }
    }
  }

  override def visitConstantString(str: ConstantString) {
    strings += (str.getBytes(this.javaClass.getConstantPool).toString)
  }

  private def isValidJavaClass(cls: String): Boolean = {
    if (cls.indexOf("/") == -1) {
      return false
    }
    if ((!cls.startsWith("(")) && (!cls.startsWith("L"))) {
      return false
    }
    if ((!cls.endsWith("V")) && (!cls.endsWith(";")) && (!cls.endsWith(")"))) {
      return false
    }
    true
  }

  private def separateClasses(utfString: String): Array[String] = {
    val tokenizer = new StringTokenizer(utfString, ";")
    val classes = Array.ofDim[String](tokenizer.countTokens())
    var i = 0
    while (tokenizer.hasMoreTokens()) {
      val cls = tokenizer.nextToken()
      if (cls.indexOf('/') != -1) {
        classes(i) = cls
        i += 1
      }
    }
    classes
  }

  private def cleanClass(cls: String): String = {
    val index = cls.lastIndexOf('L')
    var newCls = cls
    if (index != -1) {
      newCls = cls.substring(index + 1)
    }
    newCls
  }

  private def stripClassName(cls: String): String = {
    val strippedName = cls.substring(0, cls.lastIndexOf("/"))
    strippedName
  }

  def getAllImports(): List[String] = imports.toList

  def getImports(ignorePackages: List[String]): List[String] = {
    val i = this.imports.iterator()
    val imports = MutableList[String]()
    while (i.hasNext) {
      val pkg = i.next().asInstanceOf[String]
      if (ignorePackages.isEmpty == false) {
        val filter = ignorePackages.iterator()
        var filterPackage = false
        while (filter.hasNext) {
          val packageFilter = filter.next().asInstanceOf[String]
          if (pkg.startsWith(packageFilter)) {
            filterPackage = true
          }
        }
        if (!filterPackage) {
          imports += pkg
        }
      } else {
        imports += pkg
      }
    }
    imports.toList
  }

 
}