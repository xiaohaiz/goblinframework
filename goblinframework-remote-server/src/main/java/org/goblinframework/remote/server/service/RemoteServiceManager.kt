package org.goblinframework.remote.server.service

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.util.HostAndPort
import org.goblinframework.core.util.NetworkUtils
import org.goblinframework.registry.zookeeper.ZookeeperRegistryPathKeeper
import org.goblinframework.remote.core.registry.RemoteRegistryManager
import org.goblinframework.remote.server.transport.RemoteTransportServerManager
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

@Singleton
@ThreadSafe
@GoblinManagedBean("RemoteServer")
@GoblinManagedLogger("goblin.remote.server.service")
class RemoteServiceManager private constructor()
  : GoblinManagedObject(), RemoteServiceManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteServiceManager()
  }

  private val addresses = mutableListOf<HostAndPort>()
  private val keeperReference = AtomicReference<ZookeeperRegistryPathKeeper?>()

  override fun initializeBean() {
    RemoteTransportServerManager.INSTANCE.getRemoteTransportServer()?.run {
      val port = this.getTransportServer().getPort()
      val host = this.getTransportServer().getHost()!!
      if (host == NetworkUtils.ALL_HOST) {
        NetworkUtils.getLocalAddresses().forEach { addresses.add(HostAndPort(it, port)) }
      } else {
        addresses.add(HostAndPort(host, port))
      }
    }
    RemoteRegistryManager.INSTANCE.getRemoteRegistry()?.run {
      val keeper = this.createKeeper().scheduler(1, TimeUnit.MINUTES)
      keeper.initialize()
      keeperReference.set(keeper)
    }
  }

  fun unregisterAll() {}

  override fun disposeBean() {
    keeperReference.getAndSet(null)?.dispose()
  }
}