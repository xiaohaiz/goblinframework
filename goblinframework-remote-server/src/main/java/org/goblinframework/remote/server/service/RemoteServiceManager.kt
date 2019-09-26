package org.goblinframework.remote.server.service

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.common.ThreadSafe
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.core.exception.GoblinDuplicateException
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

  private val registryManager = RemoteServiceRegistryManager()
  private val lock = ReentrantReadWriteLock()
  private val services = mutableMapOf<ExposeServiceId, RemoteService>()

  fun createStaticService(bean: Any, ids: List<ExposeServiceId>) {
    lock.write {
      for (id in ids) {
        services[id]?.run {
          throw GoblinDuplicateException("Duplicated expose service id: $id")
        }
        val service = StaticRemoteService(id, bean)
        registryManager.register(service)
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
        registryManager.register(service)
        services[id] = service
      }
    }
  }

  fun remoteService(id: ExposeServiceId): RemoteService? {
    return lock.read { services[id] }
  }

  override fun initializeBean() {
    registryManager.initialize()
  }

  override fun disposeBean() {
    lock.write {
      services.values.forEach {
        it.dispose()
        registryManager.unregister(it)
      }
      services.clear()
    }
    registryManager.dispose()
  }
}