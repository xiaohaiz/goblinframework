package org.goblinframework.remote.client.service

import org.goblinframework.api.common.Disposable
import org.goblinframework.api.common.Singleton
import org.goblinframework.api.registry.Registry
import org.goblinframework.remote.client.module.config.RemoteClientConfigManager
import org.goblinframework.remote.core.module.GoblinRemoteException
import java.util.concurrent.atomic.AtomicReference

@Singleton
class RemoteClientRegistry private constructor() : Disposable {

  companion object {
    @JvmField val INSTANCE = RemoteClientRegistry()
  }

  private val registry = AtomicReference<Registry?>()

  init {
    RemoteClientConfigManager.INSTANCE.getRemoteClientConfig()?.registryLocation?.run {
      val registry = system.getRegistry(name) ?: kotlin.run {
        throw GoblinRemoteException("Registry [$this] not available")
      }
      this@RemoteClientRegistry.registry.set(registry)
    }
  }

  fun getRegistry(): Registry? {
    return registry.get()
  }

  override fun dispose() {

  }
}