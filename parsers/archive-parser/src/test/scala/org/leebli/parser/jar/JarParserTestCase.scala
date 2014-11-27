package org.leebli.parser.jar

import java.io.File
import java.io.FileInputStream

import org.junit.Test

import junit.framework.Assert

class JarParserTestCase {

  @Test
  def testParseJarOnly() {

    val cl = ClassLoader.getSystemClassLoader();
    val urls = System.getProperty("java.class.path").split(File.pathSeparator);

    println(urls.mkString(","))

    val jarUrl = urls find (_.contains("parser-jar-testproject"))

    Assert.assertTrue(jarUrl.isDefined)

    val jarFile = JarParser.parseJarFile("parser-jar-testproject.jar",
      Some(10), new FileInputStream(jarUrl.head))

    Assert.assertEquals(jarFile.name, "parser-jar-testproject.jar")
    Assert.assertEquals(jarFile.size, Some(10))
    Assert.assertFalse(jarFile.classes.isEmpty)
    Assert.assertEquals(jarFile.classes.size, 4)
    Assert.assertTrue(jarFile.mavenInfo.isDefined)
    val mavenInfo = jarFile.mavenInfo.head
    Assert.assertEquals(mavenInfo.groupId, "org.leebli.parsers")
    Assert.assertEquals(mavenInfo.artifactId, "parser-jar-testproject")
    Assert.assertEquals(mavenInfo.version, "1.0")

    println(jarFile.classes.mkString(","))

  }

  @Test
  def testParseJarAndClasses() {
    val cl = ClassLoader.getSystemClassLoader();
    val urls = System.getProperty("java.class.path").split(File.pathSeparator);

    println(urls.mkString(","))

    val jarUrl = urls find (_.contains("parser-jar-testproject"))

    Assert.assertTrue(jarUrl.isDefined)

    val jarFile = JarParser.parseJarFile("parser-jar-testproject.jar",
      Some(10), new FileInputStream(jarUrl.head), true)

    Assert.assertEquals(jarFile.name, "parser-jar-testproject.jar")
    Assert.assertEquals(jarFile.size, Some(10))
    Assert.assertFalse(jarFile.classes.isEmpty)
    Assert.assertEquals(jarFile.classes.size, 4)
    Assert.assertTrue(jarFile.mavenInfo.isDefined)
    val mavenInfo = jarFile.mavenInfo.head
    Assert.assertEquals(mavenInfo.groupId, "org.leebli.parsers")
    Assert.assertEquals(mavenInfo.artifactId, "parser-jar-testproject")
    Assert.assertEquals(mavenInfo.version, "1.0")

    println(jarFile.classes.mkString(","))

    val javaClass = jarFile.classes find (_.name == "org.leebli.parser.bytecode.test.TestAttribute")
    Assert.assertTrue(javaClass.isDefined)

  }

}