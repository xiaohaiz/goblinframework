package org.goblinframework.webmvc.interceptor

import org.goblinframework.api.function.Ordered
import org.goblinframework.webmvc.handler.RequestHandler
import org.goblinframework.webmvc.servlet.GoblinServletRequest
import org.goblinframework.webmvc.servlet.GoblinServletResponse

interface Interceptor : Ordered {

  fun getIncludePatterns(): Array<String>

  fun getExcludePatterns(): Array<String>

  fun setIncludePatterns(includePatterns: String)

  fun setExcludePatterns(excludePatterns: String)

  fun matches(lookupPath: String): Boolean

  fun preHandle(request: GoblinServletRequest,
                response: GoblinServletResponse,
                handler: RequestHandler): Boolean

  fun postHandle(request: GoblinServletRequest,
                 response: GoblinServletResponse,
                 handler: RequestHandler)

  fun afterCompletion(request: GoblinServletRequest,
                      response: GoblinServletResponse,
                      handler: RequestHandler,
                      cause: Throwable?)
}
