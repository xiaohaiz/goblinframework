package org.goblinframework.remote.server.handler

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.common.ThreadSafe
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.remote.server.module.config.RemoteServerConfig
import org.goblinframework.remote.server.module.config.RemoteServerConfigManager

@Singleton
@ThreadSafe
class RemoteServerManager private constructor()
  : GoblinManagedObject(), RemoteServerManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteServerManager()
  }

  @Synchronized
  override fun initializeBean() {
    RemoteServerConfigManager.INSTANCE.getRemoteServerConfig()?.run {
      startRemoteServer(this)
    } ?: logger.info("No [RemoteServerConfig] configured")
  }

  override fun disposeBean() {
  }

  private fun startRemoteServer(config: RemoteServerConfig) {

  }
}