package org.goblinframework.management.server

import org.goblinframework.embedded.handler.DispatchServletHandler
import org.goblinframework.embedded.resource.ClassPathStaticResourceManager
import org.goblinframework.embedded.resource.MapStaticResourceBuffer
import org.goblinframework.management.controller.ManagementControllerManager
import org.goblinframework.webmvc.handler.RequestHandlerManagerBuilder
import org.goblinframework.webmvc.setting.RequestHandlerSetting

class ManagementServletHandler private constructor() : DispatchServletHandler() {

  companion object {
    @JvmField val INSTANCE = ManagementServletHandler()
  }

  init {
    val setting = RequestHandlerSetting.builder()
        .name("ManagementServer")
        .applyControllerSetting {
          val controllers = ManagementControllerManager.INSTANCE.drain()
          it.registerControllers(*controllers.toTypedArray())
        }
        .applyInterceptorSetting {
          it.includeDefaultInterceptors(true)
        }
        .applyViewResolverSetting {
          it.registerViewResolvers(ManagementViewResolver.INSTANCE)
        }
        .build()

    val handlerManager = RequestHandlerManagerBuilder.INSTANCE.createRequestHandlerManager(setting)

    registerWelcomeFile("/index.do")
    registerRequestHandlerManager(handlerManager, ".do")

    val staticResourceBuffer = MapStaticResourceBuffer(4096)
    val staticResourceManager = ClassPathStaticResourceManager(staticResourceBuffer, "/static", "/management/static")
    registerStaticResourceManager(staticResourceManager)
  }
}