package org.goblinframework.example.embedded.interceptor

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.webmvc.handler.RequestHandler
import org.goblinframework.webmvc.interceptor.AbstractInterceptor
import org.goblinframework.webmvc.servlet.ServletRequest
import org.goblinframework.webmvc.servlet.ServletResponse
import org.slf4j.LoggerFactory

@Singleton
class StaticLogInterceptor private constructor(): AbstractInterceptor() {

  companion object {
    @JvmField val INSTANCE = StaticLogInterceptor()

    private val logger = LoggerFactory.getLogger(StaticLogInterceptor::class.java)
  }

  init {
    setIncludePatterns("/**")
  }

  override fun preHandle(request: ServletRequest, response: ServletResponse, handler: RequestHandler): Boolean {
    logger.info("StaticLogInterceptor pre-handle request [${request.uri}]")
    return true
  }

  override fun postHandle(request: ServletRequest, response: ServletResponse, handler: RequestHandler) {
    logger.info("StaticLogInterceptor post-handle request [${request.uri}]")
  }

  override fun afterCompletion(request: ServletRequest, response: ServletResponse, handler: RequestHandler, cause: Throwable?) {
    logger.info("StaticLogInterceptor after complete request [${request.uri}]")
  }
}