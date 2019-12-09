package org.goblinframework.embedded.java

import org.bson.types.ObjectId
import org.goblinframework.embedded.core.handler.ServletHandler
import org.goblinframework.embedded.core.manager.EmbeddedServerManager
import org.goblinframework.embedded.core.setting.ServerSetting
import org.goblinframework.embedded.server.EmbeddedServerMode
import org.goblinframework.test.runner.GoblinTestRunner
import org.goblinframework.webmvc.servlet.ServletRequest
import org.goblinframework.webmvc.servlet.ServletResponse
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class JdkEmbeddedServerTest {

  @Test
  fun server() {
    val name = ObjectId().toHexString()
    val setting = ServerSetting.builder()
        .name(name)
        .mode(EmbeddedServerMode.JAVA)
        .applyHandlerSetting {
          it.servletHandler(object : ServletHandler {

            override fun transformLookupPath(path: String): String {
              return path
            }

            override fun handle(request: ServletRequest, response: ServletResponse) {
            }
          })
        }
        .build()
    val server = EmbeddedServerManager.INSTANCE.createServer(setting)
    server.start()
    EmbeddedServerManager.INSTANCE.closeServer(name)
  }
}