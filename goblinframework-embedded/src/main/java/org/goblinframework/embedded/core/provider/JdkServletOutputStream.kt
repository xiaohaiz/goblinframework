package org.goblinframework.embedded.core.provider

import java.io.ByteArrayOutputStream
import javax.servlet.ServletOutputStream
import javax.servlet.WriteListener

class JdkServletOutputStream : ServletOutputStream() {

  private val flushed = ByteArrayOutputStream(512)
  private val outputStream = ByteArrayOutputStream(512)

  fun responseBody(): ByteArray {
    return flushed.toByteArray()
  }

  fun reset() {
    outputStream.reset()
  }

  override fun flush() {
    flushed.write(outputStream.toByteArray())
    outputStream.reset()
  }

  override fun isReady(): Boolean {
    return true
  }

  override fun write(b: Int) {
    outputStream.write(b)
  }

  override fun setWriteListener(writeListener: WriteListener?) {
  }
}