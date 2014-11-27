package org.leebli.model

case class MavenInfo(
  groupId: String, artifactId: String, version: String) {

  override def toString() = groupId + ":" + artifactId + ":" + version
}