package org.goblinframework.webmvc.servlet

import org.goblinframework.webmvc.util.GoblinUrlPathHelper
import org.goblinframework.webmvc.util.RequestAttribute
import org.springframework.http.server.ServletServerHttpRequest
import javax.servlet.http.HttpServletRequest

class GoblinServletServerHttpRequest(request: HttpServletRequest) : ServletServerHttpRequest(request) {

  fun getLookupPath(): String {
    return GoblinUrlPathHelper.INSTANCE.getLookupPathForRequest(servletRequest)
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