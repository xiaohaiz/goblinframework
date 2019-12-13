package org.goblinframework.embedded.server.internal

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
class JavaHttpRequestHandlerTest {

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
        .mode(EmbeddedServerMode.JAVA)
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

}