package org.goblinframework.webmvc.servlet

import org.goblinframework.webmvc.util.DefaultUrlPathHelper
import org.springframework.http.server.ServletServerHttpRequest
import javax.servlet.http.HttpServletRequest

class ServletRequest(request: HttpServletRequest) : ServletServerHttpRequest(request) {

  fun getLookupPath(): String {
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