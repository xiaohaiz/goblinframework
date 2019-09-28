package org.goblinframework.remote.client.service

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.api.core.Singleton
import org.goblinframework.remote.core.service.RemoteServiceId
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Singleton
@GoblinManagedBean(type = "RemoteClient")
class RemoteClientManager private constructor()
  : GoblinManagedObject(), RemoteClientManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteClientManager()
  }

  private val lock = ReentrantLock()
  private val buffer = ConcurrentHashMap<RemoteServiceId, RemoteClient>()

  fun getRemoteClient(serviceId: RemoteServiceId): RemoteClient {
    buffer[serviceId]?.run { return this }
    lock.withLock {
      buffer[serviceId]?.run { return this }
      val client = RemoteClient(serviceId)
      buffer[serviceId] = client
      return client
    }
  }
}