package org.goblinframework.embedded.core.provider

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import org.goblinframework.core.util.ExceptionUtils
import org.goblinframework.embedded.core.setting.ServerSetting
import org.goblinframework.http.util.HttpUtils
import org.goblinframework.webmvc.servlet.RequestAttribute
import org.goblinframework.webmvc.servlet.ServletRequest
import org.goblinframework.webmvc.servlet.ServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.net.URLDecoder

class JdkHttpRequestHandler internal constructor(private val setting: ServerSetting) : HttpHandler {

  override fun handle(exchange: HttpExchange) {
    val uri = exchange.requestURI
    val path = HttpUtils.compactContinuousSlashes(uri.rawPath)!!
    val query = uri.rawQuery
    val request = ServletRequest(JdkHttpServletRequest(exchange, path, query))
    val response = ServletResponse(JdkHttpServletResponse(exchange))
    try {
      doDispatch(path, request, response)
    } finally {
      RequestAttribute.values().forEach { request.removeAttribute(it) }
      response.flush()
      (response.servletResponse as JdkHttpServletResponse).close()
    }
  }

  private fun doDispatch(target: String,
                         request: ServletRequest,
                         response: ServletResponse) {
    val decodedPath = URLDecoder.decode(target, Charsets.UTF_8.name())
    val handlerSettings = setting.handlerSettings()
    val p = handlerSettings.lookupContextPath(decodedPath)
    val contextPath = p.left
    val path = p.right

    val handlerSetting = handlerSettings[contextPath]
    if (handlerSetting == null) {
      response.resetBuffer()
      response.setStatusCode(HttpStatus.NOT_FOUND)
      response.headers.contentLength = 0
      return
    }

    val handler = handlerSetting.servletHandler()
    val lookupPath = handler.transformLookupPath(path)
    request.setAttribute(RequestAttribute.LOOKUP_PATH, lookupPath)

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
