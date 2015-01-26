package org.leebli.parser.jar

import java.util.Collection
import org.leebli.parser.war.WarParser
import java.io.FileInputStream
import org.leebli.model.jar.JavaClass
import scala.annotation.tailrec
import org.leebli.model.jar.JavaClass

object TestEarParser extends App {

  val filepath = """C:\dev\workspaces\IBO\CIP-IBO\cip-ibo-aborigen-openejb.war"""

  val input = this.getClass().getClassLoader().getResourceAsStream("hibernate-core-3.6.6.Final.jar")

  val war = WarParser.parseWarFile("cip.war", new FileInputStream(filepath), true)

  val dep = Dependencies(war.jarFiles.flatMap(_.classes))

  println(dep.methodDependencies.toList.sorted.mkString("\n"))

  var e = Console.readLine
  while (e != "") {
    dep.tree(e).listFroms().foreach(println)
    e = Console.readLine
  }
}

case class Dependencies(classes: Iterable[JavaClass]) {
  val ignoredPkg = Set("org.apache", "java")

  lazy val methodDependencies: List[(String, String)] = for {
    cl <- classes.toList
    (from, to) <- cl.flatCalls
    if !ignoredPkg.exists(to.className.startsWith)
  } yield from.toString -> to.toString

  def tree(to: String) = DependencyTree(to)

  case class DependencyTree(to: String, visited: List[String] = Nil) {
    lazy val from: List[DependencyTree] = {
      val newDep = for {
        (f, t) <- methodDependencies
        if t == to
        if !visited.contains(f)
      } yield (f)
      val newVisited = visited ++ newDep
      newDep.map(DependencyTree(_, newVisited))
    }

    def listFroms(depth: Int = 0): List[String] =
      from.map("  " * depth + _.to) :::
        from.flatMap(_.listFroms(depth + 1))
  }
}

