package org.goblinframework.webmvc.servlet

import org.goblinframework.webmvc.util.DefaultUrlPathHelper
import org.springframework.http.server.ServletServerHttpRequest
import javax.servlet.http.HttpServletRequest

class GoblinServletRequest(request: HttpServletRequest, private val lookupPath: String? = null) : ServletServerHttpRequest(request) {

  fun getLookupPath(): String {
    lookupPath?.run { return this }
    return DefaultUrlPathHelper.INSTANCE.getLookupPathForRequest(servletRequest)
  }

}