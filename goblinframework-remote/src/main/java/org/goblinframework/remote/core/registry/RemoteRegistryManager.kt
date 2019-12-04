package org.goblinframework.remote.core.registry

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.registry.zookeeper.ZookeeperClientSetting
import org.goblinframework.remote.core.module.config.RemoteRegistryConfigManager
import java.util.concurrent.atomic.AtomicReference

@GoblinManagedBean("Remote")
class RemoteRegistryManager private constructor()
  : GoblinManagedObject(), RemoteRegistryManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteRegistryManager()
  }

  private val registryReference = AtomicReference<RemoteRegistry?>()

  override fun initializeBean() {
    RemoteRegistryConfigManager.INSTANCE.getRemoteRegistryConfig()?.run {
      val config = this
      val client = ZookeeperClientSetting.builder()
          .addresses(config.getZookeeper())
          .connectionTimeout(config.getConnectionTimeout())
          .sessionTimeout(config.getSessionTimeout())
          .serializer(config.getSerializer())
          .build()
          .createZookeeperClient()
      val registry = RemoteRegistry(client)
      registryReference.set(registry)
    }
  }

  override fun getRemoteRegistry(): RemoteRegistry? {
    return registryReference.get()
  }

  override fun disposeBean() {
    registryReference.getAndSet(null)?.dispose()
  }
}