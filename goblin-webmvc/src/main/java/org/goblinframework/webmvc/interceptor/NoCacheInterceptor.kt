package org.goblinframework.webmvc.interceptor

import org.goblinframework.webmvc.handler.RequestHandler
import org.goblinframework.webmvc.servlet.GoblinServletRequest
import org.goblinframework.webmvc.servlet.GoblinServletResponse

class NoCacheInterceptor private constructor() : AbstractInterceptor() {

  companion object {
    @JvmField val INSTANCE = NoCacheInterceptor()
  }

  init {
    setIncludePatterns("/**")
  }

  override fun preHandle(request: GoblinServletRequest, response: GoblinServletResponse, handler: RequestHandler): Boolean {
    response.headers.pragma = "no-cache"
    response.headers.expires = 1
    response.headers.cacheControl = "no-cache, no-store"
    return true
  }

}