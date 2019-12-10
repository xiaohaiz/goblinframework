package org.goblinframework.webmvc.servlet

import org.goblinframework.webmvc.util.DefaultUrlPathHelper
import org.springframework.http.server.ServletServerHttpRequest
import javax.servlet.http.HttpServletRequest

class ServletRequest(request: HttpServletRequest, private val lookupPath: String? = null) : ServletServerHttpRequest(request) {

  fun getLookupPath(): String {
    lookupPath?.run { return this }
    return DefaultUrlPathHelper.INSTANCE.getLookupPathForRequest(servletRequest)
  }

  fun setAttribute(name: RequestAttribute, attribute: Any) {
    servletRequest.setAttribute(name.attribute, attribute)
  }

  fun getAttribute(name: RequestAttribute): Any? {
    return servletRequest.getAttribute(name.attribute)
  }

  fun removeAttribute(name: RequestAttribute) {
    servletRequest.removeAttribute(name.attribute)
  }
}