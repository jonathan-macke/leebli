package org.leebli.common.http

import java.io.FileOutputStream
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.HttpGet
import java.io.File
import grizzled.slf4j.Logger
import java.io.InputStream
import org.apache.commons.io.IOUtils
import java.io.ByteArrayInputStream
import org.apache.http.impl.conn.SystemDefaultRoutePlanner
import java.net.ProxySelector
import org.apache.http.impl.client.HttpClients
import scala.util.Try
import grizzled.slf4j.Logging
import org.apache.http.config.SocketConfig
import java.lang.Exception
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.HttpHost

object FileDownload extends Logging {
  val TIMEOUT = 100000
  val CONN_TIMEOUT = 100000
  lazy val client = HttpClients.custom()
    .setDefaultRequestConfig(RequestConfig.custom().setSocketTimeout(TIMEOUT)
      .setConnectTimeout(CONN_TIMEOUT).build())
    .build();

  /**
   * Downloads the content and stores it in a temp file.
   * Returns the name of the temp file.
   */
  def download(url: String, ext: String = "bin"): (File, String) = {
    val nameRegexp = """.*(?:\/|\\)(.*)(?:\..*)""".r
    val nameRegexp(fileName) = url
    val file = File.createTempFile(fileName, s".$ext")
    val fos = new FileOutputStream(file)
    client.execute(new HttpGet(url)).getEntity.writeTo(fos)
    fos.close()
    val path = file.getAbsolutePath
    logger.info(s"$url downloaded to $path")
    (file, fileName)
  }

  /**
   * Downloads the content and stores it in a temp file.
   * Returns the name of the temp file.
   */
  def download(url: String, extension: String, targetFolder: File, targetFileName: String): (File, String) = {

    var filename = targetFileName
    if (filename == null) {
      val nameRegexp = """.*(?:\/|\\)(.*)(?:\..*)""".r
      val nameRegexp(fileName) = url

      if (extension != null) {
        filename = fileName + "." + extension
      } else {
        filename = fileName
      }
    }

    val file = new File(targetFolder, filename)
    val fos = new FileOutputStream(file)
    client.execute(new HttpGet(url)).getEntity.writeTo(fos)
    fos.close()
    val path = file.getAbsolutePath
    logger.info(s"$url downloaded to $path")
    (file, filename + "." + extension)
  }

  /**
   * Downloads the content and stores it in a temp file.
   * Returns the name of the temp file.
   */
  def download(url: String, extension: String, targetFolder: File): (File, String) = {
    download(url, extension, targetFolder, null)
  }

  def downloadAsInputStream(url: String): Either[(InputStream, String), String] = {
    
    val nameRegexp = """.*(?:\/|\\)(.*\..*)""".r  // filename with extension

    url match {
      case nameRegexp(fileName) =>
        try {
          val request = new HttpGet(url)

          val response = client.execute(request)

          info(s"HTTP request : $request")
          info(request.getAllHeaders.mkString("\n"))

          info(s"HTTP response : $response")
          info(response.getAllHeaders().mkString("\n"))

          response.getStatusLine.getStatusCode match {
            case 200 => Left((response.getEntity.getContent, fileName))
            case 404 => Right(s"URL $url not found")
            case 403 => Right(s"The server is not able to access this URL $url")
            case 500 =>
              error(s"cannot access $url")
              Right("internal error")
            case errorStatus => Right(s"error with code $errorStatus")
          }

        } catch {
          case e: Exception =>
            warn(s"error when accessing $url", e)
            Right(s"cannot access $url")
        }

      case _ => Right(s"not able to recognize filename in url $url")
    }

  }
}