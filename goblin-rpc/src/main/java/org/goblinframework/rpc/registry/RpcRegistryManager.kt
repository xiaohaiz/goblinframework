package org.goblinframework.rpc.registry

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.registry.zookeeper.ZookeeperClientSetting
import org.goblinframework.rpc.module.config.RpcRegistryConfigManager
import java.util.concurrent.atomic.AtomicReference

@GoblinManagedBean("Rpc")
class RpcRegistryManager private constructor()
  : GoblinManagedObject(), RpcRegistryManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RpcRegistryManager()
  }

  private val registryReference = AtomicReference<RpcRegistry?>()

  override fun initializeBean() {
    RpcRegistryConfigManager.INSTANCE.getRpcRegistryConfig()?.run {
      val config = this
      val client = ZookeeperClientSetting.builder()
          .addresses(config.getZookeeper())
          .connectionTimeout(config.getConnectionTimeout())
          .sessionTimeout(config.getSessionTimeout())
          .serializer(config.getSerializer())
          .build()
          .createZookeeperClient()
      val registry = RpcRegistry(client)
      registryReference.set(registry)
    }
  }

  override fun getRpcRegistry(): RpcRegistry? {
    return registryReference.get()
  }

  override fun disposeBean() {
    registryReference.getAndSet(null)?.dispose()
  }
}