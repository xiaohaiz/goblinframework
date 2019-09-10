package org.goblinframework.webmvc.interceptor

import org.goblinframework.api.common.Ordered
import org.goblinframework.webmvc.handler.RequestHandler
import org.goblinframework.webmvc.servlet.GoblinServletServerHttpRequest
import org.goblinframework.webmvc.servlet.GoblinServletServerHttpResponse

interface Interceptor : Ordered {

  fun getIncludePatterns(): Array<String>

  fun getExcludePatterns(): Array<String>

  fun setIncludePatterns(includePatterns: String)

  fun setExcludePatterns(excludePatterns: String)

  fun matches(lookupPath: String): Boolean

  fun preHandle(request: GoblinServletServerHttpRequest,
                response: GoblinServletServerHttpResponse,
                handler: RequestHandler): Boolean

  fun postHandle(request: GoblinServletServerHttpRequest,
                 response: GoblinServletServerHttpRequest,
                 handler: RequestHandler)

  fun afterCompletion(request: GoblinServletServerHttpRequest,
                      response: GoblinServletServerHttpRequest,
                      handler: RequestHandler,
                      cause: Throwable?)
}
