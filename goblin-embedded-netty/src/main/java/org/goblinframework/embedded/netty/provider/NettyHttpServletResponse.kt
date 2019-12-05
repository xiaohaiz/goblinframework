package org.goblinframework.embedded.netty.provider

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.DefaultFullHttpResponse
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpVersion
import org.goblinframework.embedded.core.servlet.AbstractHttpServletResponse
import org.goblinframework.embedded.core.servlet.SendError
import org.goblinframework.webmvc.util.HttpContentTypes
import org.springframework.http.HttpHeaders
import java.io.Closeable
import java.io.PrintWriter
import java.util.concurrent.atomic.AtomicReference
import javax.servlet.ServletOutputStream

class NettyHttpServletResponse(private val ctx: ChannelHandlerContext)
  : AbstractHttpServletResponse(), Closeable {

  private val outputStream = NettyServletOutputStream()
  private val writer = PrintWriter(outputStream)
  private val response = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
  private val sendError = AtomicReference<SendError>()

  override fun getOutputStream(): ServletOutputStream {
    return outputStream
  }

  override fun getWriter(): PrintWriter {
    return writer
  }

  override fun getStatus(): Int {
    return response.status().code()
  }

  override fun setStatus(sc: Int) {
    response.status = HttpResponseStatus.valueOf(sc)
  }

  override fun getHeaders(name: String): MutableCollection<String> {
    return response.headers().getAll(name)
  }

  override fun addHeader(name: String, value: String) {
    response.headers().add(name, value)
  }

  override fun getHeader(name: String): String? {
    return response.headers().get(name)
  }

  override fun getHeaderNames(): MutableCollection<String> {
    return response.headers().names()
  }

  override fun setHeader(name: String, value: String) {
    response.headers().set(name, value)
  }

  override fun sendError(sc: Int, msg: String?) {
    sendError.set(SendError(sc, msg))
  }

  override fun sendError(sc: Int) {
    sendError(sc, null)
  }

  override fun flushBuffer() {
    writer.flush()
  }

  override fun resetBuffer() {
    outputStream.reset()
  }

  override fun reset() {
    outputStream.reset()
    response.headers().clear()
  }

  override fun close() {
    sendError.get()?.run {
      reset()
      status = this.sc
      this.msg?.run {
        contentType = HttpContentTypes.TEXT_PLAIN
        val bs = this.toByteArray(Charsets.UTF_8)
        outputStream.write(bs)
        setContentLength(bs.size)
      }
    }

    try {
      flushBuffer()
      val content = outputStream.content()
      val contentLen = content.size
      if (!containsHeader(HttpHeaders.CONTENT_LENGTH)) {
        setContentLength(contentLen)
      }
      response.content().writeBytes(content)
    } finally {
      ctx.writeAndFlush(response)
    }
  }
}