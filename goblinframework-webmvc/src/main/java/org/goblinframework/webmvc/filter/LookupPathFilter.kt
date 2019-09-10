package org.goblinframework.webmvc.filter

import org.goblinframework.webmvc.servlet.RequestAttribute
import org.goblinframework.webmvc.util.DefaultUrlPathHelper
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LookupPathFilter : OncePerRequestFilter() {

  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    var localSet = false
    try {
      if (request.getAttribute(RequestAttribute.LOOKUP_PATH.attribute) == null) {
        val lookupPath = DefaultUrlPathHelper.INSTANCE.getLookupPathForRequest(request)
        request.setAttribute(RequestAttribute.LOOKUP_PATH.attribute, lookupPath)
        localSet = true
      }
      filterChain.doFilter(request, response)
    } finally {
      if (localSet) {
        request.removeAttribute(RequestAttribute.LOOKUP_PATH.attribute)
      }
    }
  }
}