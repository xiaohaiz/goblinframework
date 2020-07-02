package org.goblinframework.embedded.server.internal

import org.apache.commons.io.output.ByteArrayOutputStream
import javax.servlet.ServletOutputStream
import javax.servlet.WriteListener

class JavaServletOutputStream : ServletOutputStream() {

  private val outputStream = ByteArrayOutputStream()

  fun content(): ByteArray {
    return outputStream.toByteArray()
  }

  fun reset() {
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