package org.goblinframework.management.server

import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.embedded.core.EmbeddedServerMode
import org.goblinframework.embedded.core.manager.EmbeddedServerManager
import org.goblinframework.embedded.core.setting.ServerSetting

@GoblinManagedBean(type = "management")
class ManagementServer internal constructor()
  : GoblinManagedObject(), ManagementServerMXBean {

  companion object {
    private const val SERVER_NAME = "GoblinManagementServer"
  }

  override fun initializeBean() {
    val setting = ServerSetting.builder()
        .name(SERVER_NAME)
        .mode(EmbeddedServerMode.JDK)
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
    serverManager.closeServer(SERVER_NAME)
  }
}