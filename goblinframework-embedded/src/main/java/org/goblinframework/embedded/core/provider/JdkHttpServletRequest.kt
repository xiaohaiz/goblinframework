package org.goblinframework.embedded.core.provider

import com.sun.net.httpserver.HttpExchange
import org.apache.commons.io.IOUtils
import org.goblinframework.embedded.core.servlet.AbstractHttpServletRequest
import org.goblinframework.http.util.HttpUtils
import org.springframework.util.LinkedMultiValueMap
import java.util.*
import javax.servlet.ServletInputStream

class JdkHttpServletRequest(private val exchange: HttpExchange,
                            private val path: String,
                            private val query: String?) : AbstractHttpServletRequest() {

  companion object {
    private const val FORM_CONTENT_TYPE = "application/x-www-form-urlencoded"
  }

  private val inputStream = JdkServletInputStream(exchange)
  private val parameters = LinkedMultiValueMap<String, String>()

  init {
    query?.run {
      HttpUtils.parseMultiQueryString(this).forEach { (t, u) ->
        parameters.addAll(t, u.toList())
      }
    }
    if ("POST".equals(method, true)
        && (contentType ?: "").contains(FORM_CONTENT_TYPE, true)) {
      val content = inputStream.content()
      val s = IOUtils.toString(content, Charsets.UTF_8.name())
      HttpUtils.parseMultiQueryString(s).forEach { (t, u) ->
        parameters.addAll(t, u.toList())
      }
    }
  }

  override fun getMethod(): String {
    return exchange.requestMethod
  }

  override fun getInputStream(): ServletInputStream {
    return inputStream
  }

  override fun getRequestURI(): String {
    return path
  }

  override fun getQueryString(): String? {
    return query
  }

  override fun getHeaderNames(): Enumeration<String> {
    return Collections.enumeration(exchange.requestHeaders.keys)
  }

  override fun getHeaders(name: String): Enumeration<String> {
    val values = exchange.requestHeaders[name] ?: Collections.emptyList()
    return Collections.enumeration(values)
  }

  override fun getHeader(name: String): String? {
    return exchange.requestHeaders.getFirst(name)
  }

  override fun getParameterNames(): Enumeration<String> {
    return Collections.enumeration(parameters.keys)
  }

  override fun getParameter(name: String): String? {
    return parameters.getFirst(name)
  }

  override fun getParameterMap(): Map<String, Array<String>> {
    val map = mutableMapOf<String, Array<String>>()
    parameters.forEach { (t, u) -> map[t] = u.toTypedArray() }
    return Collections.unmodifiableMap(map)
  }

  override fun getParameterValues(name: String): Array<String>? {
    return parameters[name]?.toTypedArray()
  }
}