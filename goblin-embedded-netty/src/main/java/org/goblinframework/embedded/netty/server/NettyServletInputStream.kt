package org.goblinframework.embedded.netty.server

import java.io.ByteArrayInputStream
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream

class NettyServletInputStream(private val requestBody: ByteArray) : ServletInputStream() {

  private val inputStream = ByteArrayInputStream(requestBody)

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