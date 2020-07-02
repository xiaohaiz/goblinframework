package org.goblinframework.example.embedded.interceptor

import org.goblinframework.webmvc.handler.RequestHandler
import org.goblinframework.webmvc.interceptor.AbstractInterceptor
import org.goblinframework.webmvc.servlet.GoblinServletRequest
import org.goblinframework.webmvc.servlet.GoblinServletResponse
import org.slf4j.LoggerFactory

class ManagedLogInterceptor: AbstractInterceptor() {

  companion object {
    private val logger = LoggerFactory.getLogger(ManagedLogInterceptor::class.java)
  }

  init {
    setIncludePatterns("/**")
  }

  override fun preHandle(request: GoblinServletRequest, response: GoblinServletResponse, handler: RequestHandler): Boolean {
    logger.info("ManagedLogInterceptor pre-handle request [${request.servletRequest.requestURI}]")
    return true
  }

  override fun postHandle(request: GoblinServletRequest, response: GoblinServletResponse, handler: RequestHandler) {
    logger.info("ManagedLogInterceptor post-handle request [${request.servletRequest.requestURI}]")
  }

  override fun afterCompletion(request: GoblinServletRequest, response: GoblinServletResponse, handler: RequestHandler, cause: Throwable?) {
    logger.info("ManagedLogInterceptor after complete request [${request.servletRequest.requestURI}]")
  }
}