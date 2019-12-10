package org.goblinframework.management.server

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.embedded.server.EmbeddedServerId
import org.goblinframework.embedded.server.EmbeddedServerManager
import org.goblinframework.embedded.server.EmbeddedServerMode
import org.goblinframework.embedded.setting.ServerSetting

@GoblinManagedBean(type = "Management")
class ManagementServer internal constructor()
  : GoblinManagedObject(), ManagementServerMXBean {

  companion object {
    private const val SERVER_NAME = "goblin.management.server"
  }

  override fun initializeBean() {
    val setting = ServerSetting.builder()
        .name(SERVER_NAME)
        .mode(EmbeddedServerMode.JAVA)
        .applyHandlerSetting {
          it.contextPath("/")
          it.servletHandler(ManagementServletHandler.INSTANCE)
        }
        .build()
    val serverManager = EmbeddedServerManager.INSTANCE
    serverManager.createServer(setting).start()
  }

  override fun disposeBean() {
    val serverManager = EmbeddedServerManager.INSTANCE
    serverManager.closeServer(EmbeddedServerId(EmbeddedServerMode.JAVA, SERVER_NAME))
  }
}