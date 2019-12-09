package org.goblinframework.embedded.java

import com.sun.net.httpserver.HttpExchange
import org.apache.commons.io.IOUtils
import java.io.ByteArrayInputStream
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream

class JdkServletInputStream(exchange: HttpExchange) : ServletInputStream() {

  private val requestBody: ByteArray = exchange.requestBody.use { IOUtils.toByteArray(it) }
  private val inputStream: ByteArrayInputStream = ByteArrayInputStream(requestBody)

  fun content(): ByteArray {
    return requestBody
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