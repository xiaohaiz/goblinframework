package org.goblinframework.remote.server.transport

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.remote.server.module.config.RemoteServerConfigManager
import java.util.concurrent.atomic.AtomicReference

@GoblinManagedBean("RemoteServer")
class RemoteTransportServerManager private constructor()
  : GoblinManagedObject(), RemoteTransportServerManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteTransportServerManager()
  }

  private val serverReference = AtomicReference<RemoteTransportServer?>()

  override fun initializeBean() {
    val serverConfig = RemoteServerConfigManager.INSTANCE.getRemoteServerConfig()
    serverReference.set(RemoteTransportServer(serverConfig))
  }

  override fun getRemoteTransportServer(): RemoteTransportServerMXBean? {
    return serverReference.get()
  }

  override fun disposeBean() {
    serverReference.getAndSet(null)?.dispose()
  }
}