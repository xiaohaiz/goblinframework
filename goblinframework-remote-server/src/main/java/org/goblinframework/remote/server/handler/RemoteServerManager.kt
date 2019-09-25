package org.goblinframework.remote.server.handler

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.common.ThreadSafe
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.remote.server.module.config.RemoteServerConfigManager
import java.util.concurrent.atomic.AtomicReference

@Singleton
@ThreadSafe
class RemoteServerManager private constructor()
  : GoblinManagedObject(), RemoteServerManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteServerManager()
  }

  private val server = AtomicReference<RemoteServer?>()

  @Synchronized
  override fun initializeBean() {
    RemoteServerConfigManager.INSTANCE.getRemoteServerConfig()?.run {
      server.set(RemoteServer(this))
    } ?: logger.info("No [RemoteServerConfig] configured")
  }

  override fun disposeBean() {
    server.getAndSet(null)?.dispose()
  }
}