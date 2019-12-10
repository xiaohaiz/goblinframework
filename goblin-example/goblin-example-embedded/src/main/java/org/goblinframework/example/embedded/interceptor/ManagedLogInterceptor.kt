package org.goblinframework.example.embedded.interceptor

import org.goblinframework.webmvc.handler.RequestHandler
import org.goblinframework.webmvc.interceptor.AbstractInterceptor
import org.goblinframework.webmvc.servlet.ServletRequest
import org.goblinframework.webmvc.servlet.ServletResponse
import org.slf4j.LoggerFactory

class ManagedLogInterceptor: AbstractInterceptor() {

  companion object {
    private val logger = LoggerFactory.getLogger(ManagedLogInterceptor::class.java)
  }

  init {
    setIncludePatterns("/**")
  }

  override fun preHandle(request: ServletRequest, response: ServletResponse, handler: RequestHandler): Boolean {
    logger.info("ManagedLogInterceptor pre-handle request [${request.uri}]")
    return true
  }

  override fun postHandle(request: ServletRequest, response: ServletResponse, handler: RequestHandler) {
    logger.info("ManagedLogInterceptor post-handle request [${request.uri}]")
  }

  override fun afterCompletion(request: ServletRequest, response: ServletResponse, handler: RequestHandler, cause: Throwable?) {
    logger.info("ManagedLogInterceptor after complete request [${request.uri}]")
  }
}