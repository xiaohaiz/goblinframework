package org.goblinframework.webmvc.servlet

import org.goblinframework.webmvc.container.ServletSpringContainer
import org.goblinframework.webmvc.exception.NoRequestHandlerFoundException
import org.goblinframework.webmvc.handler.RequestHandlerManager
import org.goblinframework.webmvc.handler.RequestHandlerManagerBuilder
import org.goblinframework.webmvc.setting.RequestHandlerSetting
import repackages.org.springframework.web.servlet.FrameworkServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

open class DispatchServlet : FrameworkServlet() {

  private lateinit var requestHandlerManager: RequestHandlerManager

  override fun initFrameworkServlet() {
    val context = webApplicationContext
    val name = "$servletName-servlet"
    val setting = RequestHandlerSetting.builder()
        .name(name)
        .applyControllerSetting {
          it.applicationContext(context)
        }
        .applyInterceptorSetting {
          it.includeDefaultInterceptors(true)
          it.applicationContext(context)
        }
        .applyViewResolverSetting {
          it.applicationContext(context)
        }
        .build()
    requestHandlerManager = RequestHandlerManagerBuilder.INSTANCE.createRequestHandlerManager(setting)
  }

  override fun getContextClass(): Class<*> {
    return ServletSpringContainer::class.java
  }

  override fun doOptions(request: HttpServletRequest?, response: HttpServletResponse?) {
    processRequest(request, response)
  }

  override fun doService(request: HttpServletRequest, response: HttpServletResponse) {
    val servletRequest = GoblinServletRequest(request)
    val servletResponse = GoblinServletResponse(response)
    val requestHandler = requestHandlerManager.lookup(servletRequest, servletResponse)
        ?: throw NoRequestHandlerFoundException()
    requestHandler.invoke()
  }
}