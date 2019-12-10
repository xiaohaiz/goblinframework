package org.goblinframework.example.embedded

import org.goblinframework.bootstrap.core.StandaloneServer
import org.goblinframework.core.container.GoblinSpringContainer
import org.goblinframework.core.container.SpringContainer
import org.goblinframework.embedded.handler.DispatchServletHandler
import org.goblinframework.embedded.handler.ServletHandler
import org.goblinframework.embedded.resource.ClassPathStaticResourceManager
import org.goblinframework.embedded.resource.MapStaticResourceBuffer
import org.goblinframework.embedded.server.EmbeddedServerManager
import org.goblinframework.embedded.server.EmbeddedServerMode
import org.goblinframework.embedded.setting.ServerSetting
import org.goblinframework.example.embedded.interceptor.StaticLogInterceptor
import org.goblinframework.webmvc.handler.RequestHandlerManagerBuilder
import org.goblinframework.webmvc.servlet.ServletRequest
import org.goblinframework.webmvc.servlet.ServletResponse
import org.goblinframework.webmvc.setting.RequestHandlerSetting
import org.springframework.http.MediaType
import javax.servlet.http.HttpServletResponse

@GoblinSpringContainer("/config/goblin-example-embedded.xml")
class Server : StandaloneServer() {

  override fun doService(container: SpringContainer?) {
    val ctx = container!!.applicationContext
    val handler = DispatchServletHandler()
    handler.registerWelcomeFile("/index.goblin")

    val setting = RequestHandlerSetting.builder()
        .name("GOBLIN")
        .applyControllerSetting { it.applicationContext(ctx) }
        .applyInterceptorSetting {
          it.includeDefaultInterceptors(true)
          it.applicationContext(ctx)// for managed
          it.registerInterceptors(StaticLogInterceptor.INSTANCE)// for static
        }
        .applyViewResolverSetting { it.applicationContext(ctx) }
        .build()
    val handlerManager = RequestHandlerManagerBuilder.INSTANCE.createRequestHandlerManager(setting)
    handler.registerRequestHandlerManager(handlerManager, ".goblin")

    val resourceBuffer = MapStaticResourceBuffer(4096)
    val resourceManager = ClassPathStaticResourceManager(resourceBuffer, "/static", "/WEB-INF/static")
    handler.registerStaticResourceManager(resourceManager)

    val serverSetting = ServerSetting.builder()
        .name("GOBLIN")
        .mode(EmbeddedServerMode.JETTY)
        .applyNetworkSetting {
          it.port(9797)
        }
        .applyHandlerSetting {
          it.contextPath("/")
          it.servletHandler(handler)
        }
        .nextHandlerSetting()
        .applyHandlerSetting {
          it.contextPath("/abc")
          it.servletHandler(object : ServletHandler {
            override fun transformLookupPath(path: String): String {
              return path
            }

            override fun handle(request: ServletRequest, response: ServletResponse) {
              response.headers.contentLength = 5
              response.headers.contentType = MediaType.TEXT_PLAIN

              response.servletResponse.status = HttpServletResponse.SC_OK
              response.servletResponse.writer.println("hello")

              response.flush()
            }
          })
        }
        .build()
    EmbeddedServerManager.INSTANCE.createServer(serverSetting).start()
  }

}

fun main(args: Array<String>) {
  Server().bootstrap(args)
}