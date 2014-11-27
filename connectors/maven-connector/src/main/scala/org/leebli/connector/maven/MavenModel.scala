package org.leebli.connector.maven

import org.apache.maven.model.Model
import org.apache.maven.model.Repository
import org.apache.maven.model.RepositoryPolicy
import java.util.ArrayList
import java.util.UUID
import org.apache.maven.model.Build
import org.apache.maven.model.Plugin
import org.codehaus.plexus.util.xml.Xpp3Dom
import scala.collection.JavaConversions._
import java.nio.file.Path
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import resource._
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import org.apache.maven.model.io.xpp3.MavenXpp3Writer
import java.util.Collections
import org.apache.maven.artifact.resolver.ArtifactResolutionRequest
import org.apache.maven.repository.LocalArtifactRepository
import org.apache.maven.RepositoryUtils
import org.apache.maven.project.DefaultDependencyResolutionRequest
import org.apache.maven.project.MavenProject
import org.sonatype.aether.RepositorySystemSession
import org.apache.maven.project.ProjectDependenciesResolver
import java.util.LinkedHashSet
import org.apache.maven.artifact.Artifact
import org.apache.maven.cli.MavenCli
import org.apache.maven.model.Dependency

/**
 * enhance existing maven model
 */
case class MavenModel(pomFile: Path) extends Model {
  val reader = new MavenXpp3Reader()
  for (
    in <- managed(Files.newInputStream(pomFile))
  ) {
    val pomMaven = reader.read(in)
    setArtifactId(pomMaven.getArtifactId)
    setGroupId(pomMaven.getGroupId)
    setVersion(pomMaven.getVersion)
    setParent(pomMaven.getParent)
    setBuild(pomMaven.getBuild)
    setDependencies(pomMaven.getDependencies)
    setCiManagement(pomMaven.getCiManagement)
    setContributors(pomMaven.getContributors)
    setDescription(pomMaven.getDescription)
    setInceptionYear(pomMaven.getInceptionYear)
    setDependencyManagement(pomMaven.getDependencyManagement)
    setDevelopers(pomMaven.getDevelopers)
    setIssueManagement(pomMaven.getIssueManagement)
    setLicenses(pomMaven.getLicenses)
    setMailingLists(pomMaven.getMailingLists)
    setModelEncoding(pomMaven.getModelEncoding)
    setModelVersion(pomMaven.getModelVersion)
    setModules(pomMaven.getModules)
    setName(pomMaven.getName)
    setOrganization(pomMaven.getOrganization)
    setPackaging(pomMaven.getPackaging)
    setPluginRepositories(pomMaven.getPluginRepositories)
    setPrerequisites(pomMaven.getPrerequisites)
    setProfiles(pomMaven.getProfiles)
    setProperties(pomMaven.getProperties)
    setReporting(pomMaven.getReporting)
    setReports(pomMaven.getReports)
    setRepositories(pomMaven.getRepositories)
    setScm(pomMaven.getScm)
    setUrl(pomMaven.getUrl)
  }
  val projectDir = pomFile.getParent

  def addRepository(url: String, isSnapshot: Boolean = false) {

    if (getRepositories == null) {
      setRepositories(new ArrayList[Repository])
    }

    val repository = new Repository
    repository.setId(UUID.randomUUID().toString)
    repository.setUrl(url)

    if (isSnapshot) {
      val policy = new RepositoryPolicy
      policy.setEnabled(true)
      repository.setSnapshots(policy)
    }

    getRepositories.add(repository)
  }

  def addPluginRepository(url: String, isSnapshot: Boolean = false) {

    if (getPluginRepositories == null) {
      setPluginRepositories(new ArrayList[Repository])
    }

    val repository = new Repository
    repository.setId(UUID.randomUUID().toString)
    repository.setUrl(url)

    if (isSnapshot) {
      val policy = new RepositoryPolicy
      policy.setEnabled(true)
      repository.setSnapshots(policy)
    }

    getPluginRepositories.add(repository)
  }

  /**
   * given a goal groupId:artifactId:version:goal
   */
  def addOrUpdatePlugin(goal: String, configuration: Map[String, String]) {
    val elementOfGoal = goal.split(":")
    val groupIdPlugin = elementOfGoal(0)
    val artifactIdPlugin = elementOfGoal(1)
    val versionPlugin = elementOfGoal(2)

    addOrUpdatePlugin(groupIdPlugin, artifactIdPlugin, versionPlugin, configuration)
  }

  /**
   * given a goal groupId:artifactId:version:goal
   */
  def addOrUpdatePlugin(goal: String, configuration: Xpp3Dom) {
    val elementOfGoal = goal.split(":")
    val groupIdPlugin = elementOfGoal(0)
    val artifactIdPlugin = elementOfGoal(1)
    val versionPlugin = elementOfGoal(2)

    addOrUpdatePlugin(groupIdPlugin, artifactIdPlugin, versionPlugin, configuration)
  }

  def addDependencyToPlugin(pluginGroupId: String, pluginArtifactId: String, pluginVersion: String, dep: Dependency) {
    val key = pluginGroupId + ":" + pluginArtifactId + ":" + pluginVersion
    val plugin = getBuild.getPluginsAsMap().get(key)
    if (plugin != null) {
      plugin.addDependency(dep)
    }
  }

  def addDependencyToPlugin(goal: String, groupId: String, artifactId: String, version: String) {

    val elementOfGoal = goal.split(":")
    val groupIdPlugin = elementOfGoal(0)
    val artifactIdPlugin = elementOfGoal(1)
    val versionPlugin = elementOfGoal(2)

    val dep = new Dependency
    dep.setArtifactId(artifactId)
    dep.setGroupId(groupId)
    dep.setVersion(version)

    addDependencyToPlugin(groupIdPlugin, artifactIdPlugin, versionPlugin, dep: Dependency)
  }

  /**
   * add or update a plugin
   */
  def addOrUpdatePlugin(groupId: String, artifactId: String, version: String, configuration: Map[String, String]) {
    val pluginMap = {
      if (getBuild == null) {
        setBuild(new Build())
      }
      getBuild.getPluginsAsMap()
    }

    val key = groupId + ":" + artifactId + ":" + version

    val plugin: Plugin = {
      if (pluginMap.get(key) == null) {
        val pluginCxf = new Plugin
        pluginCxf.setArtifactId(artifactId)
        pluginCxf.setGroupId(groupId)
        pluginCxf.setVersion(version)
        pluginMap.put(key, pluginCxf)
      }
      pluginMap.get(key)
    }

    if (configuration != null) {

      val domConfig = new Xpp3Dom("configuration")
      for ((key, value) <- configuration) {
        val element = new Xpp3Dom(key)
        element.setValue(value)
        domConfig.addChild(element)
      }

      plugin.setConfiguration(domConfig)

    }

    getBuild.setPlugins(pluginMap.values().toList)

  }

  /**
   * add or update a plugin
   */
  def addOrUpdatePlugin(groupId: String, artifactId: String, version: String, configuration: Xpp3Dom) {
    val pluginMap = {
      if (getBuild == null) {
        setBuild(new Build())
      }
      getBuild.getPluginsAsMap()
    }

    val key = groupId + ":" + artifactId + ":" + version

    val plugin: Plugin = {
      if (pluginMap.get(key) == null) {
        val pluginCxf = new Plugin
        pluginCxf.setArtifactId(artifactId)
        pluginCxf.setGroupId(groupId)
        pluginCxf.setVersion(version)
        pluginMap.put(key, pluginCxf)
      }
      pluginMap.get(key)
    }

    plugin.setConfiguration(configuration)

    getBuild.setPlugins(pluginMap.values().toList)

  }

  def setOutputDir(dir: String) {
    if (getBuild == null) {
      setBuild(new Build)
    }
    getBuild.setOutputDirectory("target")
  }

  def save() {
    val writer = new MavenXpp3Writer()
    for (
      outputStream <- managed(Files.newOutputStream(pomFile, StandardOpenOption.CREATE))
    ) {
      writer.write(outputStream, this)
    }
  }

  def runMavenCommand(goal: String, debug: Boolean = false) {
    val cli = new MavenCli();

    val args = if (debug) Array(goal, "-X") else Array(goal)

    cli.doMain(args, projectDir.toFile.getAbsolutePath, System.out, System.err)

  }

}

