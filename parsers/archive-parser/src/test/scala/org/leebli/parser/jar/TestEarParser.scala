package org.leebli.parser.jar

import java.util.Collection
import org.leebli.parser.war.WarParser
import java.io.FileInputStream
import org.leebli.model.jar.JavaClass
import scala.annotation.tailrec
import org.leebli.model.jar.JavaClass
import org.leebli.model.jar.ClassMethod
import org.leebli.model.jar.ClassMethod
import org.leebli.model.jar.ClassMethod

object TestEarParser extends App {
  //net.atos.wlp.cms.core.cardmanagement.cardcontract.CardContract.getActiveVersion
  //  val filepath = """C:\dev\workspaces\IBO\CIP-IBO\cip-ibo-aborigen-openejb.war"""
  val filepath = """C:\Utilisateurs\a518291\Documents\Mes fichiers reçus\WLP-BOE_5.5.2.024.009-20140924_CA-CP_IBO.ear"""

  val war = WarParser.parseWarFile("cip.war", new FileInputStream(filepath), true)

  val dep = Dependencies(war.jarFiles.flatMap(_.classes))

  println(dep.methodDependencies.toList.mkString("\n"))

  var e = Console.readLine
  while (e != "") {
    val elts = e.split("\\.").toList
    val method = elts.last
    val cl = elts.take(elts.size - 1).mkString(".")
    dep.tree(ClassMethod(cl, method)).listFroms()
    e = Console.readLine
  }
}

case class Dependencies(classes: Iterable[JavaClass]) {
  val ignoredPkg = Set("org.apache", "java")

  lazy val directChildren: Map[String, List[String]] = {
    var children = collection.mutable.Map.empty[String, List[String]].withDefaultValue(Nil)
    for {
      c <- classes
      p <- c.parents
    } {
      children += p -> (c.name :: children(p))
    }
    children.toMap.withDefaultValue(Nil)
  }

  def isChildOf(parent: String, child: String): Boolean =
    parent == child || directChildren(parent).exists(isChildOf(_, child))

  lazy val methodDependencies: List[(ClassMethod, ClassMethod)] = for {
    cl <- classes.toList
    (from, to) <- cl.flatCalls
    if !ignoredPkg.exists(to.className.startsWith)
  } yield from -> to

  def tree(to: ClassMethod) = DependencyTree(to)

  def isCompatibleCall(declared: ClassMethod, candidate: ClassMethod) =
    declared.method == candidate.method && isChildOf(declared.className, candidate.className)

  case class DependencyTree(to: ClassMethod, visited: List[ClassMethod] = Nil) {
    lazy val from: List[DependencyTree] = {
      val newDep = for {
        (f, t) <- methodDependencies
        if isCompatibleCall(t, to)
        if !visited.contains(f)
      } yield (f)
      val newVisited = visited ++ newDep
      newDep.map(DependencyTree(_, newVisited))
    }

    def listFroms(depth: Int = 1): Unit = {
      for {
        f <- from
      } {
        println("  " * depth + f.to)
        f.listFroms(depth + 1)
      }
    }
  }
}

