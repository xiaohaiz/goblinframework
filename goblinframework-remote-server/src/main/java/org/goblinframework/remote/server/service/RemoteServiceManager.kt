package org.goblinframework.remote.server.service

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.common.ThreadSafe
import org.goblinframework.api.core.GoblinManagedBean
import org.goblinframework.api.core.GoblinManagedObject
import org.goblinframework.api.registry.GoblinRegistryException
import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.core.exception.GoblinDuplicateException
import org.goblinframework.remote.server.handler.RemoteServerManager
import org.goblinframework.remote.server.module.config.RemoteServerConfigManager
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
@ThreadSafe
@GoblinManagedBean(type = "RemoteServer")
class RemoteServiceManager private constructor()
  : GoblinManagedObject(), RemoteServiceManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteServiceManager()
  }

  private val registryManager = AtomicReference<RemoteServiceRegistryManager?>()
  private val lock = ReentrantReadWriteLock()
  private val services = mutableMapOf<ExposeServiceId, RemoteService>()

  fun createStaticService(bean: Any, ids: List<ExposeServiceId>) {
    lock.write {
      for (id in ids) {
        services[id]?.run {
          throw GoblinDuplicateException("Duplicated expose service id: $id")
        }
        val service = StaticRemoteService(id, bean)
        registryManager.get()?.register(service)
        services[id] = service
      }
    }
  }

  fun createManagedService(cmb: ContainerManagedBean, ids: List<ExposeServiceId>) {
    lock.write {
      for (id in ids) {
        services[id]?.run {
          throw GoblinDuplicateException("Duplicated expose service id: $id")
        }
        val service = ManagedRemoteService(id, cmb)
        registryManager.get()?.register(service)
        services[id] = service
      }
    }
  }

  fun remoteService(id: ExposeServiceId): RemoteService? {
    return lock.read { services[id] }
  }

  fun unregisterAll() {
    registryManager.get()?.dispose()
  }

  override fun initializeBean() {
    val server = RemoteServerManager.INSTANCE.getRemoteServer()
        ?: kotlin.run {
          logger.debug("No RemoteServer started, ignore")
          return
        }
    val location = RemoteServerConfigManager.INSTANCE.getRemoteServerConfig()?.registryLocation
        ?: kotlin.run {
          logger.warn("No [RemoteServer.registry] configured")
          return
        }
    val registry = location.system.getRegistry(location.name)
        ?: throw GoblinRegistryException("No registry [$location] available")
    registryManager.set(RemoteServiceRegistryManager(server, registry))
  }

  override fun disposeBean() {
    lock.write {
      services.values.forEach {
        it.dispose()
        registryManager.get()?.unregister(it)
      }
      services.clear()
    }
    registryManager.getAndSet(null)?.dispose()
  }
}