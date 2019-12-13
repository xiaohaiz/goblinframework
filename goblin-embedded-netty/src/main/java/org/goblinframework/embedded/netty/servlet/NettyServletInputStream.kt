package org.goblinframework.embedded.netty.servlet

import io.netty.handler.codec.http.FullHttpRequest
import java.io.ByteArrayInputStream
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream

class NettyServletInputStream(request: FullHttpRequest) : ServletInputStream() {

  private val requestBody: ByteArray
  private val inputStream: ByteArrayInputStream

  init {
    val content = request.content()
    if (content.isReadable) {
      requestBody = ByteArray(content.readableBytes())
      content.readBytes(requestBody)
    } else {
      requestBody = ByteArray(0)
    }
    inputStream = ByteArrayInputStream(requestBody)
  }

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