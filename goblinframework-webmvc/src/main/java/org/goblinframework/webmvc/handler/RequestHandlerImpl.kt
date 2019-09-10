package org.goblinframework.webmvc.handler

import org.goblinframework.core.conversion.ConversionService
import org.goblinframework.webmvc.exception.RequestHandlerInvocationException
import org.goblinframework.webmvc.interceptor.Interceptor
import org.goblinframework.webmvc.interceptor.InterceptorLocator
import org.goblinframework.webmvc.mapping.controller.ControllerMapping
import org.goblinframework.webmvc.servlet.ServletRequest
import org.goblinframework.webmvc.servlet.ServletResponse
import org.goblinframework.webmvc.setting.RequestHandlerSetting
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import java.util.concurrent.atomic.AtomicInteger
import javax.servlet.ServletException

class RequestHandlerImpl(val setting: RequestHandlerSetting,
                         val request: ServletRequest,
                         val response: ServletResponse,
                         val lookupPath: String,
                         val bestMatchingPattern: String,
                         val controllerMapping: ControllerMapping) : RequestHandler {

  companion object {
    private val logger = LoggerFactory.getLogger(RequestHandlerImpl::class.java)
  }

  private val interceptors: Array<Interceptor>
  private val interceptorIndex = AtomicInteger(-1)

  init {
    val locator = InterceptorLocator(setting.interceptorSetting())
    interceptors = locator.locate(lookupPath).toTypedArray()
  }

  override fun invoke() {
    try {
      if (!preHandle()) {
        return
      }
      doInvoke()
      postHandle()
      afterCompletion(null)
    } catch (ex: Throwable) {
      afterCompletion(ex)
      throw ex as? ServletException ?: RequestHandlerInvocationException(ex)
    }
  }

  private fun preHandle(): Boolean {
    if (interceptors.isEmpty()) {
      return true
    }
    for (i in interceptors.indices) {
      val interceptor = interceptors[i]
      if (!interceptor.preHandle(request, response, this)) {
        afterCompletion(null)
        return false
      }
      interceptorIndex.set(i)
    }
    return true
  }

  private fun postHandle() {
    if (interceptors.isEmpty()) {
      return
    }
    for (i in interceptors.indices.reversed()) {
      val interceptor = interceptors[i]
      interceptor.postHandle(request, response, this)
    }
  }

  private fun afterCompletion(cause: Throwable?) {
    if (interceptors.isEmpty()) {
      return
    }
    for (i in this.interceptorIndex.get() downTo 0) {
      val interceptor = interceptors[i]
      try {
        interceptor.afterCompletion(request, response, this, cause)
      } catch (ex: Throwable) {
        logger.warn("Interceptor.afterCompletion", ex)
      }
    }
  }

  private fun doInvoke() {
    if (request.method === HttpMethod.OPTIONS) {
      val allowedMethods = controllerMapping.methodMapping.allowMethods
          .mapNotNull { ConversionService.INSTANCE.convert(it, HttpMethod::class.java) }
          .toSet()
      response.headers.allow = allowedMethods
      response.headers.contentLength = 0
      return
    }


  }
}