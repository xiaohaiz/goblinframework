package org.goblinframework.embedded.jetty.server

import org.goblinframework.core.util.RandomUtils
import org.goblinframework.core.web.CoreRestTemplate
import org.goblinframework.embedded.handler.ServletHandler
import org.goblinframework.embedded.server.EmbeddedServerManager
import org.goblinframework.embedded.server.EmbeddedServerMode
import org.goblinframework.embedded.setting.ServerSetting
import org.goblinframework.test.runner.GoblinTestRunner
import org.goblinframework.webmvc.servlet.GoblinServletRequest
import org.goblinframework.webmvc.servlet.GoblinServletResponse
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import java.util.concurrent.atomic.AtomicReference
import javax.servlet.http.HttpServletResponse

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class JettyHttpRequestHandlerTest {

  @Test
  fun getContextPath() {
    val contextPath = AtomicReference<String?>()
    val handler: ServletHandler = object : ServletHandler {
      override fun transformLookupPath(path: String): String {
        return path
      }

      override fun handle(request: GoblinServletRequest, response: GoblinServletResponse) {
        contextPath.set(request.servletRequest.contextPath)
        response.sendTextResponse(HttpServletResponse.SC_OK, "getContextPath")
      }
    }
    val name = RandomUtils.nextObjectId()
    val setting = ServerSetting.builder()
        .name(name)
        .mode(EmbeddedServerMode.JETTY)
        .applyNetworkSetting {
          it.host("127.0.0.1")
        }
        .applyHandlerSetting {
          it.contextPath("/getContextPath")
          it.servletHandler(handler)
        }
        .build()
    val server = EmbeddedServerManager.INSTANCE.createServer(setting)
    server.start()
    val template = CoreRestTemplate.getInstance()
    template.getForObject("http://127.0.0.1:${server.getPort()}/getContextPath/a", String::class.java)
    assertEquals("/getContextPath", contextPath.get())
  }

  @Test
  fun getRequestURI() {
    val requestURI = AtomicReference<String?>()
    val handler: ServletHandler = object : ServletHandler {
      override fun transformLookupPath(path: String): String {
        return path
      }

      override fun handle(request: GoblinServletRequest, response: GoblinServletResponse) {
        requestURI.set(request.servletRequest.requestURI)
        response.sendTextResponse(HttpServletResponse.SC_OK, "getRequestURI")
      }
    }
    val name = RandomUtils.nextObjectId()
    val setting = ServerSetting.builder()
        .name(name)
        .mode(EmbeddedServerMode.JETTY)
        .applyNetworkSetting {
          it.host("127.0.0.1")
        }
        .applyHandlerSetting {
          it.contextPath("/getRequestURI")
          it.servletHandler(handler)
        }
        .build()
    val server = EmbeddedServerManager.INSTANCE.createServer(setting)
    server.start()
    val template = CoreRestTemplate.getInstance()
    template.getForObject("http://127.0.0.1:${server.getPort()}/getRequestURI/a/b/c", String::class.java)
    assertEquals("/getRequestURI/a/b/c", requestURI.get())
  }
}