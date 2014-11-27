package org.leebli.connector.maven

import java.nio.file.Path
import java.nio.file.Paths
import org.apache.commons.io.FileUtils
import java.nio.file.Files
import java.util.Properties
import org.apache.maven.settings.Settings
import java.io.File

case class MavenConfiguration(
  settingsXml: Path = Paths.get(FileUtils.getUserDirectoryPath + "/.m2/settings.xml"),
  var localRepo: Path = Paths.get(FileUtils.getUserDirectoryPath + "/.m2/repository/")) {

  def settings: Settings = {
    val settings = new Settings()
    settings.setLocalRepository(localRepo.toFile().getAbsolutePath)
    settings
  }

  def settings_=(settings: Settings) {
    localRepo = Paths.get(settings.getLocalRepository)
  }

  def isSettingsXmlExist = Files.exists(settingsXml)

  def settingsXmlAsFile = settingsXml.toFile()
}