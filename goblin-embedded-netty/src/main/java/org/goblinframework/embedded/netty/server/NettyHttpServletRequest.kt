package org.goblinframework.embedded.netty.server

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.QueryStringDecoder
import io.netty.handler.ssl.SslHandler
import org.apache.commons.io.IOUtils
import org.goblinframework.core.util.StringUtils
import org.goblinframework.embedded.servlet.AbstractHttpServletRequest
import org.springframework.util.LinkedMultiValueMap
import java.net.InetSocketAddress
import java.util.*
import javax.servlet.ServletInputStream

class NettyHttpServletRequest(private val ctx: ChannelHandlerContext,
                              private val request: FullHttpRequest,
                              private val decoder: QueryStringDecoder,
                              private val contextPath: String,
                              private val path: String)
  : AbstractHttpServletRequest() {

  private val inputStream = NettyServletInputStream(request)
  private val parameters = LinkedMultiValueMap<String, String>()
  private val query: String

  init {
    val uri = decoder.uri()
    query = if (StringUtils.contains(uri, "?")) {
      StringUtils.substringAfter(uri, "?")
    } else {
      StringUtils.EMPTY
    }
    decoder.parameters().forEach { (name, values) ->
      values.forEach { parameters.add(name, it) }
    }
    if (request.method() === HttpMethod.POST
        && (contentType ?: "").contains("application/x-www-form-urlencoded", true)) {
      val bytes = inputStream.content()
      val s = IOUtils.toString(bytes, Charsets.UTF_8.name())
      val postFormDecoder = QueryStringDecoder(s, Charsets.UTF_8, false)

      postFormDecoder.parameters().forEach { (t, u) ->
        u.forEach { parameters.add(t, it) }
      }
    }
  }

  override fun isSecure(): Boolean {
    return ctx.pipeline().get(SslHandler::class.java) != null
  }

  override fun getScheme(): String {
    return if (isSecure) "https" else "http"
  }

  override fun getRemoteHost(): String {
    return (ctx.channel().remoteAddress() as InetSocketAddress).hostName
  }

  override fun getRemotePort(): Int {
    return (ctx.channel().remoteAddress() as InetSocketAddress).port
  }

  override fun getContextPath(): String {
    return contextPath
  }

  override fun getInputStream(): ServletInputStream {
    return inputStream
  }

  override fun getHeaders(name: String): Enumeration<String> {
    return Collections.enumeration(request.headers().getAll(name))
  }

  override fun getHeader(name: String): String? {
    return request.headers().get(name)
  }

  override fun getHeaderNames(): Enumeration<String> {
    return Collections.enumeration(request.headers().names())
  }

  override fun getQueryString(): String {
    return query
  }

  override fun getMethod(): String {
    return request.method().name()
  }

  override fun getRequestURI(): String {
    return path
  }

  override fun getParameter(name: String): String? {
    return parameters.getFirst(name)
  }

  override fun getParameterNames(): Enumeration<String> {
    return Collections.enumeration(parameters.keys)
  }

  override fun getParameterValues(name: String): Array<String>? {
    return parameters.getValue(name)?.toTypedArray() ?: return null
  }

  override fun getParameterMap(): MutableMap<String, Array<String>> {
    val map = mutableMapOf<String, Array<String>>()
    parameters.forEach { (t, u) -> map[t] = u.toTypedArray() }
    return map
  }
}