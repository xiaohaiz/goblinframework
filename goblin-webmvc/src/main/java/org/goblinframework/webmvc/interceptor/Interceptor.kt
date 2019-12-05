package org.goblinframework.webmvc.interceptor

import org.goblinframework.api.function.Ordered
import org.goblinframework.webmvc.handler.RequestHandler
import org.goblinframework.webmvc.servlet.ServletRequest
import org.goblinframework.webmvc.servlet.ServletResponse

interface Interceptor : Ordered {

  fun getIncludePatterns(): Array<String>

  fun getExcludePatterns(): Array<String>

  fun setIncludePatterns(includePatterns: String)

  fun setExcludePatterns(excludePatterns: String)

  fun matches(lookupPath: String): Boolean

  fun preHandle(request: ServletRequest,
                response: ServletResponse,
                handler: RequestHandler): Boolean

  fun postHandle(request: ServletRequest,
                 response: ServletResponse,
                 handler: RequestHandler)

  fun afterCompletion(request: ServletRequest,
                      response: ServletResponse,
                      handler: RequestHandler,
                      cause: Throwable?)
}
