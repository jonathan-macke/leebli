package org.leebli.connector.maven

import java.io.File
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.Collections
import java.util.Properties
import org.apache.commons.io.FileUtils
import org.apache.maven.settings.Settings
import org.apache.maven.settings.io.xpp3.SettingsXpp3Writer
import scala.Tuple2
import org.leebli.common.http.FileDownload
import scala.collection.JavaConversions._
import resource._
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import org.apache.maven.model.Model
import org.apache.maven.settings.io.xpp3.SettingsXpp3Reader
import org.leebli.parser.bytecode.ClassDataLoader
import org.apache.maven.model.Build
import java.util.ArrayList
import org.apache.maven.model.Plugin
import org.apache.maven.model.io.xpp3.MavenXpp3Writer
import org.codehaus.plexus.util.xml.Xpp3Dom
import org.apache.maven.model.Dependency
import org.apache.maven.model.Repository
import java.util.UUID
import org.apache.commons.compress.compressors.CompressorStreamFactory
import java.util.zip.ZipInputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import scala.Array
import java.io.IOException
import org.apache.maven.model.RepositoryPolicy

object MavenConnector {

 
  def launchMavenCommandOnRemoteURL(url: String, goal: String, configuration: MavenConfiguration, projectDir: File, debug: Boolean = false): MavenModel = {

    var pomTuple: (File, String) = (null, null)
    var jarTuple: (File, String) = (null, null)
    if (url.endsWith("pom")) {
      pomTuple = FileDownload.download(url, null, projectDir, "pom.xml")
      jarTuple = FileDownload.download(url.substring(0, url.length() - 3) + "jar", "jar", projectDir)
    } else {
      jarTuple = FileDownload.download(url, "jar", projectDir)
      pomTuple = FileDownload.download(url.substring(0, url.length() - 3) + "pom", null, projectDir, "pom.xml")
    }
    launchMavenCommand(projectDir, pomTuple, jarTuple, goal, configuration, debug)
  }

  def defineSettings(configuration: MavenConfiguration) {
    if (!configuration.isSettingsXmlExist) {

      val writer = new SettingsXpp3Writer()
      try {
        for (outputStream <- managed(Files.newOutputStream(configuration.settingsXml, StandardOpenOption.CREATE))) {
          writer.write(outputStream, configuration.settings)
        }
      } catch {
        case ex: Exception => ex.printStackTrace()
      }
    } else {
      val reader = new SettingsXpp3Reader()
      try {
        for (inputstream <- managed(Files.newInputStream(configuration.settingsXml))) {
          configuration.settings = reader.read(inputstream)
        }
      } catch {
        case ex: Exception => ex.printStackTrace()
      }
    }
  }

  def launchMavenCommand(projectDir: File, pomTuple: (File, String), jarTuple: (File, String),
    goal: String, configuration: MavenConfiguration, debug: Boolean = false): MavenModel = {

    try {

      val mavenModel = MavenModel(pomTuple._1.toPath)
      mavenModel.setOutputDir("target")


      val loader = new ClassDataLoader();
      loader.read(jarTuple._1.toURI());

      val listOfClasses = loader.getClassesWithAnnotation("javax/jws/WebService")

      //ZipUtil.unZipIt(jarTuple._1, new File(projectDir, "target"))

      val domConfig = new Xpp3Dom("configuration")

      val classNames = new Xpp3Dom("className")
      for (clazz <- listOfClasses) {
        val param = new Xpp3Dom("param")
        param.setValue(clazz.getName().replaceAll("/", "."))
        classNames.addChild(param)
      }
      domConfig.addChild(classNames)

      val element = new Xpp3Dom("genWsdl")
      element.setValue("true")
      domConfig.addChild(element)

      mavenModel.addOrUpdatePlugin(goal, domConfig)

      val groupId = if (mavenModel.getGroupId != null) mavenModel.getGroupId else mavenModel.getParent.getGroupId
      val artifactId = if (mavenModel.getArtifactId != null) mavenModel.getArtifactId else mavenModel.getParent.getArtifactId
      val version = if (mavenModel.getVersion != null) mavenModel.getVersion else mavenModel.getParent.getVersion

      mavenModel.addDependencyToPlugin(goal, groupId, artifactId, version)

      mavenModel.save

      mavenModel.runMavenCommand(goal, debug)

      mavenModel
    } catch {
      case ex: Exception => ex.printStackTrace(); throw ex

    }

  }

}