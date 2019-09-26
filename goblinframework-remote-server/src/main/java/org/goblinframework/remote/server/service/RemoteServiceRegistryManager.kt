package org.goblinframework.remote.server.service

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.registry.GoblinRegistryException
import org.goblinframework.api.registry.RegistryPathWatchdog
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.remote.server.module.config.RemoteServerConfigManager
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

@Singleton
@GoblinManagedBean(type = "RemoteServer")
class RemoteServiceRegistryManager private constructor()
  : GoblinManagedObject(), RemoteServiceRegistryManagerMXBean {

  companion object {
    val INSTANCE = RemoteServiceRegistryManager()
  }

  private val watchdog = AtomicReference<RegistryPathWatchdog?>()

  override fun initializeBean() {
    val location = RemoteServerConfigManager.INSTANCE.getRemoteServerConfig()?.registryLocation
        ?: kotlin.run {
          logger.warn("No RemoteServer or registry configured")
          return
        }
    val registry = location.system.getRegistry(location.name)
        ?: throw GoblinRegistryException("No registry [$location] available")
    val watchdog = registry.createPathWatchdog().scheduler(1, TimeUnit.MINUTES)
    watchdog.initialize()
    this.watchdog.set(watchdog)
  }

  override fun disposeBean() {
    watchdog.getAndSet(null)?.dispose()
  }
}