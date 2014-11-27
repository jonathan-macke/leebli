package org.leebli.parser.util

import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import org.apache.commons.compress.archivers.ArchiveInputStream
import org.apache.commons.compress.archivers.ArchiveEntry

object ZipInputStreamIterator {
  implicit def zipInputStreamToIterator(zin: ZipInputStream): Iterator[ZipEntry] = new Iterator[ZipEntry] {
    var last: ZipEntry = null
    def hasNext = {
      last = zin.getNextEntry
      last != null
    }
    def next = last
  }

  implicit def archiveInputStreamToIterator(zin: ArchiveInputStream): Iterator[ArchiveEntry] = new Iterator[ArchiveEntry] {
    var last: ArchiveEntry = null
    def hasNext = {
      last = zin.getNextEntry()
      last != null
    }
    def next = last
  }

}