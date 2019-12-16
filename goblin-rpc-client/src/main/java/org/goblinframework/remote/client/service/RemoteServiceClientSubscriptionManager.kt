package org.goblinframework.remote.client.service

import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.api.core.ReferenceCount
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.system.GoblinSystem
import org.goblinframework.core.util.GoblinReferenceCount
import org.goblinframework.core.util.NetworkUtils
import org.goblinframework.core.util.SystemUtils
import org.goblinframework.registry.zookeeper.ZookeeperRegistryPathKeeper
import org.goblinframework.remote.core.registry.RemoteRegistry
import org.goblinframework.rpc.registry.RpcClientNode
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@ThreadSafe
@GoblinManagedBean("RemoteClient")
@GoblinManagedLogger("goblin.remote.client.service")
class RemoteServiceClientSubscriptionManager
internal constructor(val registry: RemoteRegistry)
  : GoblinManagedObject(), RemoteServiceClientSubscriptionManagerMXBean {

  private val path: String
  private val keeper: ZookeeperRegistryPathKeeper
  private val lock = ReentrantLock()
  private val buffer = mutableMapOf<String, ReferenceCount>()

  init {
    val node = RpcClientNode()
    node.clientId = GoblinSystem.applicationId()
    node.clientName = GoblinSystem.applicationName()
    node.clientHost = NetworkUtils.getLocalAddress()
    node.clientPid = SystemUtils.getJvmPID()
    path = node.toPath()
    keeper = registry.createKeeper().scheduler(1, TimeUnit.MINUTES)
    keeper.initialize()
  }

  internal fun watch(serviceInterface: String) {
    lock.withLock {
      var rc = buffer[serviceInterface]
      if (rc == null) {
        rc = GoblinReferenceCount()
        buffer[serviceInterface] = rc
        val fullPath = "/goblin/remote/service/$serviceInterface/client/$path"
        keeper.watch(fullPath, true, null)
      }
      rc.retain()
    }
  }

  internal fun unwatch(serviceInterface: String) {
    lock.withLock {
      val rc = buffer[serviceInterface] ?: return
      if (rc.release()) {
        buffer.remove(serviceInterface)
        val fullPath = "/goblin/remote/service/$serviceInterface/client/$path"
        keeper.unwatch(fullPath)
      }
    }
  }

  override fun dispose() {
    keeper.dispose()
  }
}