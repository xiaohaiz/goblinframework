package org.goblinframework.embedded.core.provider

import com.sun.net.httpserver.HttpExchange
import org.apache.commons.io.IOUtils
import java.io.ByteArrayInputStream
import java.io.InputStream
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream

class JdkServletInputStream(exchange: HttpExchange) : ServletInputStream() {

  private val requestBody: ByteArray
  private val inputStream: InputStream

  init {
    val `in` = exchange.requestBody
    requestBody = IOUtils.toByteArray(`in`)
    inputStream = ByteArrayInputStream(requestBody)
  }

  fun requestBodyAsString(): String {
    return requestBody.toString(Charsets.UTF_8)
  }

  override fun isReady(): Boolean {
    return true
  }

  override fun isFinished(): Boolean {
    return inputStream.available() <= 0
  }

  override fun read(): Int {
    return inputStream.read()
  }

  override fun setReadListener(readListener: ReadListener?) {
  }
}
