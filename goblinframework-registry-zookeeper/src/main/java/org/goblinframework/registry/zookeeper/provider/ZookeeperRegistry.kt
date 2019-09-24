package org.goblinframework.registry.zookeeper.provider

import org.goblinframework.api.common.Disposable
import org.goblinframework.api.registry.Registry
import org.goblinframework.api.registry.RegistryLocation
import org.goblinframework.api.registry.RegistryStateListener
import org.goblinframework.api.registry.RegistrySystem
import org.goblinframework.registry.zookeeper.client.ZookeeperClient
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

internal class ZookeeperRegistry
internal constructor(private val client: ZookeeperClient) : Registry, Disposable {

  private val location = RegistryLocation(RegistrySystem.ZKP, client.config.getName())

  private val stateListenerLock = ReentrantLock()
  private val stateListeners = IdentityHashMap<RegistryStateListener, ZookeeperStateListener>()

  override fun location(): RegistryLocation {
    return location
  }

  override fun subscribeStateListener(listener: RegistryStateListener) {
    stateListenerLock.withLock {
      stateListeners[listener]?.run { return }
      val zsl = ZookeeperStateListener(listener)
      client.nativeClient().subscribeStateChanges(zsl)
      stateListeners[listener] = zsl
    }
  }

  override fun unsubscribeStateListener(listener: RegistryStateListener) {
    stateListenerLock.withLock {
      stateListeners.remove(listener)?.let {
        client.nativeClient().unsubscribeStateChanges(it)
      }
    }
  }

  override fun dispose() {
    stateListenerLock.withLock {
      stateListeners.values.forEach { client.nativeClient().unsubscribeStateChanges(it) }
      stateListeners.clear()
    }
  }
}