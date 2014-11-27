package org.leebli.parser.util

import org.junit.Ignore
import org.junit.Test

import junit.framework.Assert

class RegexUtilTestCase {

  @Test @Ignore
  def testRegex1() = {

    val Some((folder, name, version)) =
      RegexUtil.identifyFolderAndJarNameAndVersion("WEB-INF/lib/hibernate-jpa-2.0-api-1.0.1.Final.jar")
    Assert.assertEquals("WEB-INF/lib", folder)
    Assert.assertEquals("hibernate-jpa-2.0-api", name)
    Assert.assertEquals("1.0.1.Final", version)

  }

  @Test
  def testRegex2() = {
    val Some((folder, name, version)) =
      RegexUtil.identifyFolderAndJarNameAndVersion("WEB-INF/lib/mysql-connector-java-5.0.4-bin.jar")
    Assert.assertEquals("WEB-INF/lib", folder)
    Assert.assertEquals("mysql-connector-java", name)
    Assert.assertEquals("5.0.4-bin", version)

  }

  @Test
  def testRegex3() = {
    val Some((folder, name, version)) =
      RegexUtil.identifyFolderAndJarNameAndVersion("WEB-INF/lib/openejb-core-3.1.4.xa-3.jar")
    Assert.assertEquals("WEB-INF/lib", folder)
    Assert.assertEquals("openejb-core", name)
    Assert.assertEquals("3.1.4.xa-3", version)

  }

  @Test
  def testRegex4() = {
    val Some((folder, name, version)) =
      RegexUtil.identifyFolderAndJarNameAndVersion("WEB-INF/lib/postgresql-9.1-901.jdbc4.jar")
    Assert.assertEquals("WEB-INF/lib", folder)
    Assert.assertEquals("postgresql", name)
    Assert.assertEquals("9.1-901.jdbc4", version)

  }

  @Test
  def testRegex5() = {
    val Some((folder, name, version)) =
      RegexUtil.identifyFolderAndJarNameAndVersion("/javax.inject-1.jar")
    Assert.assertEquals("", folder)
    Assert.assertEquals("javax.inject", name)
    Assert.assertEquals("1", version)

  }

  @Test
  def testRegex6() = {
    val Some((folder, name, version)) =
      RegexUtil.identifyFolderAndJarNameAndVersion("WEB-INF/lib/javax.ws.rs-api-2.0-m10.jar")
    Assert.assertEquals("WEB-INF/lib", folder)
    Assert.assertEquals("javax.ws.rs-api", name)
    Assert.assertEquals("2.0-m10", version)

  }

  @Test
  def testRegex7() = {
    val Some((folder, name, version)) =
      RegexUtil.identifyFolderAndJarNameAndVersion("WEB-INF/lib/common-0.0.2-SNAPSHOT.jar")
    Assert.assertEquals("WEB-INF/lib", folder)
    Assert.assertEquals("common", name)
    Assert.assertEquals("0.0.2-SNAPSHOT", version)

  }

  @Test
  def testRegex8() = {
    val Some((folder, name, version)) =
      RegexUtil.identifyFolderAndJarNameAndVersion("WEB-INF/lib/guava-r05.jar")
    Assert.assertEquals("WEB-INF/lib", folder)
    Assert.assertEquals("guava", name)
    Assert.assertEquals("r05", version)

  }

  @Test
  def testRegex9() = {
    val Some((folder, name, version)) =
      RegexUtil.identifyFolderAndJarNameAndVersion("WEB-INF/lib/xa-rm-tapestry-core-2.6.2.jar")
    Assert.assertEquals("WEB-INF/lib", folder)
    Assert.assertEquals("xa-rm-tapestry-core", name)
    Assert.assertEquals("2.6.2", version)

  }

  @Test
  def testRegexWithoutFolder() = {
    val Some((folder, name, version)) =
      RegexUtil.identifyFolderAndJarNameAndVersion("javax.inject-1.jar")
    Assert.assertEquals("", folder)
    Assert.assertEquals("javax.inject", name)
    Assert.assertEquals("1", version)

  }

  @Test
  def testRegexWithError() = {
    Assert.assertTrue(RegexUtil.identifyFolderAndJarNameAndVersion("WEB-INF/lib/guava-.jar").isEmpty)
  }
}
