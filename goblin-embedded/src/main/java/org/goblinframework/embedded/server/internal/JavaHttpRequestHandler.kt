package org.goblinframework.embedded.server.internal

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import org.goblinframework.core.util.ExceptionUtils
import org.goblinframework.core.util.HttpUtils
import org.goblinframework.core.util.StringUtils
import org.goblinframework.embedded.handler.ServletHandler
import org.goblinframework.embedded.setting.ServerSetting
import org.goblinframework.webmvc.servlet.GoblinServletRequest
import org.goblinframework.webmvc.servlet.GoblinServletResponse
import org.goblinframework.webmvc.servlet.RequestAttribute
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.net.URLDecoder

class JavaHttpRequestHandler internal constructor(private val setting: ServerSetting) : HttpHandler {

  override fun handle(exchange: HttpExchange) {
    val uri = exchange.requestURI
    val path = HttpUtils.compactContinuousSlashes(uri.rawPath)!!
    val query = uri.rawQuery

    val response = GoblinServletResponse(JavaHttpServletResponse(exchange))

    val decodedPath = URLDecoder.decode(path, Charsets.UTF_8.name())
    val handlerSettings = setting.handlerSettings()
    val parsed = handlerSettings.lookupContextPath(decodedPath)
    val contextPath = parsed.left
    var lookupPath = parsed.right

    val handlerSetting = handlerSettings[contextPath] ?: run {
      response.resetBuffer()
      response.setStatusCode(HttpStatus.NOT_FOUND)
      response.headers.contentLength = 0
      return@handle
    }

    val handler = handlerSetting.servletHandler()
    lookupPath = handler.transformLookupPath(lookupPath)

    val request = GoblinServletRequest(JavaHttpServletRequest(exchange, contextPath, lookupPath, query))
    val requestAcceptCompression = isRequestAcceptGzip(request)
    val enableCompression = handlerSetting.enableCompression()
    (response.servletResponse as JavaHttpServletResponse).enableCompression(requestAcceptCompression && enableCompression)

    try {
      doDispatch(handler, request, response)
    } finally {
      RequestAttribute.values().forEach { request.removeAttribute(it) }
      response.flush()
      (response.servletResponse as JavaHttpServletResponse).close()
    }
  }

  private fun isRequestAcceptGzip(request: GoblinServletRequest): Boolean {
    val acceptEncoding = request.servletRequest.getHeader(HttpHeaders.ACCEPT_ENCODING)
    if (acceptEncoding.isNullOrBlank()) {
      return false
    }
    return StringUtils.split(acceptEncoding, ",")
        .filter { it.isNotBlank() }
        .map { it.trim() }
        .map { it.toLowerCase() }
        .firstOrNull { it == "gzip" } != null
  }

  private fun doDispatch(handler: ServletHandler,
                         request: GoblinServletRequest,
                         response: GoblinServletResponse) {
    request.setAttribute(RequestAttribute.LOOKUP_PATH, request.getLookupPath())

    try {
      handler.handle(request, response)
    } catch (ex: Exception) {
      val stackTrace = ExceptionUtils.getStackTrace(ex).toByteArray(Charsets.UTF_8)
      response.resetBuffer()
      response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
      response.headers.contentLength = stackTrace.size.toLong()
      response.headers.contentType = MediaType(MediaType.TEXT_PLAIN, Charsets.UTF_8)
      response.body.write(stackTrace)
    }
  }
}
