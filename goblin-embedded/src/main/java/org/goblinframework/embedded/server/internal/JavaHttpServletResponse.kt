package org.goblinframework.embedded.server.internal

import com.sun.net.httpserver.HttpExchange
import org.goblinframework.embedded.servlet.AbstractHttpServletResponse
import org.springframework.http.HttpHeaders
import java.io.Closeable
import java.io.PrintWriter
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletResponse

class JavaHttpServletResponse(private val exchange: HttpExchange)
  : AbstractHttpServletResponse(), Closeable {

  private val outputStream = JavaServletOutputStream()
  private val writer = PrintWriter(outputStream, true)
  private val status = AtomicInteger(HttpServletResponse.SC_OK)
  private val committed = AtomicBoolean()

  override fun getOutputStream(): ServletOutputStream {
    return outputStream
  }

  override fun getWriter(): PrintWriter {
    return writer
  }

  override fun resetBuffer() {
    check(!isCommitted)
    outputStream.reset()
  }

  override fun reset() {
    check(!isCommitted)
    exchange.responseHeaders.clear()
    resetBuffer()
  }

  override fun flushBuffer() {
    committed.set(true)
  }

  override fun isCommitted(): Boolean {
    return committed.get()
  }

  override fun getStatus(): Int {
    return status.get()
  }

  override fun setStatus(sc: Int) {
    status.set(sc)
  }

  override fun setStatus(sc: Int, sm: String?) {
    setStatus(sc)
  }

  override fun getHeaderNames(): MutableCollection<String> {
    return exchange.responseHeaders.keys
  }

  override fun getHeaders(name: String): MutableCollection<String> {
    return exchange.responseHeaders[name] ?: mutableListOf()
  }

  override fun getHeader(name: String): String? {
    return exchange.responseHeaders.getFirst(name)
  }

  override fun containsHeader(name: String): Boolean {
    return getHeader(name) != null
  }

  override fun addHeader(name: String, value: String) {
    exchange.responseHeaders.add(name, value)
  }

  override fun setHeader(name: String, value: String) {
    exchange.responseHeaders.set(name, value)
  }

  override fun close() {
    Closeable { exchange.close() }.use {
      val sc = getStatus()
      val content = outputStream.content()
      var contentLen = content.size
      if (sc == HttpServletResponse.SC_NOT_MODIFIED) {
        contentLen = -1
      } else {
        if (getHeader(HttpHeaders.CONTENT_LENGTH) == null) {
          setContentLength(contentLen)
        }
      }
      exchange.sendResponseHeaders(sc, contentLen.toLong())
      exchange.responseBody.write(content)
      exchange.responseBody.flush()
    }
  }
}