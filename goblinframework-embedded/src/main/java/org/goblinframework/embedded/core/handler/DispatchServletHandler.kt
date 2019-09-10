package org.goblinframework.embedded.core.handler

import org.goblinframework.webmvc.servlet.ServletRequest
import org.goblinframework.webmvc.servlet.ServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.util.*

open class DispatchServletHandler : FilterSupport(), ServletHandler {

  override fun transformLookupPath(path: String): String {
    return doTransformLookupPath(path) ?: path
  }

  override fun handle(request: ServletRequest, response: ServletResponse) {
    createFilterChain {
      internalHandle(request, response)
    }.doFilter(request.servletRequest, response.servletResponse)
  }

  private fun internalHandle(request: ServletRequest,
                             response: ServletResponse) {

    val lookupPath = request.getLookupPath()
    val controllerManager = getRequestHandlerManager(lookupPath)
    if (controllerManager != null) {
      val controller = controllerManager.lookup(request, response)
      if (controller == null) {
        response.setStatusCode(HttpStatus.NOT_FOUND)
        response.headers.contentLength = 0
        return
      }
      controller.invoke()
      return
    }

    val staticResourceManager = getStaticResourceManager(lookupPath)
    if (staticResourceManager == null) {
      response.setStatusCode(HttpStatus.NOT_FOUND)
      response.headers.contentLength = 0
      return
    }
    val staticResource = staticResourceManager.lookup(lookupPath)
    if (staticResource?.content == null) {
      response.setStatusCode(HttpStatus.NOT_FOUND)
      response.headers.contentLength = 0
      return
    }

    val ims = request.headers.ifModifiedSince
    if (ims >= 0 && staticResource.lastModified <= ims + 999) {
      response.setStatusCode(HttpStatus.NOT_MODIFIED)
      response.headers.contentLength = 0
      return
    }
    response.setStatusCode(HttpStatus.OK)

    val contentType = try {
      staticResource.contentType?.run { MediaType.parseMediaType(this) }
          ?: kotlin.run { MediaType.APPLICATION_OCTET_STREAM }
    } catch (ex: Exception) {
      MediaType.APPLICATION_OCTET_STREAM
    }
    response.headers.contentType = contentType
    response.headers.contentLength = staticResource.content.size.toLong()

    val calendar = Calendar.getInstance()
    calendar.add(Calendar.SECOND, 604800)

    response.headers.cacheControl = "max-age=604800"
    response.headers.expires = calendar.time.time
    if (staticResource.lastModified >= 0) {
      response.headers.lastModified = staticResource.lastModified
    }

    response.body.write(staticResource.content)
  }
}