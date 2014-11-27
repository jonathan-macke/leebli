package org.leebli.parser.war

import org.junit.Test

import junit.framework.Assert

class WarParserTestCase {
  @Test
  def testParseJarOnly() {
    val warStream = getClass().getClassLoader()
      .getResourceAsStream("parser-war-testproject-1.0.war")

    val warFile = WarParser.parseWarFile("parser-war-testproject.war", warStream)

    Assert.assertEquals(warFile.name, "parser-war-testproject.war")
    Assert.assertFalse(warFile.jarFiles.isEmpty)
    Assert.assertEquals(warFile.jarFiles.size, 1)
    Assert.assertTrue(warFile.mavenInfo.isDefined)
    val mavenInfo = warFile.mavenInfo.head
    Assert.assertEquals(mavenInfo.groupId, "org.leebli.parsers")
    Assert.assertEquals(mavenInfo.artifactId, "parser-war-testproject")
    Assert.assertEquals(mavenInfo.version, "1.0")

    println(warFile.jarFiles.toString)

  }

  
}