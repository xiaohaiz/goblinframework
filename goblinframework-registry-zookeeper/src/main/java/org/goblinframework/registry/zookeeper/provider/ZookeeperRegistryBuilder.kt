package org.goblinframework.registry.zookeeper.provider

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.registry.Registry
import org.goblinframework.api.registry.RegistryBuilder
import org.goblinframework.registry.zookeeper.client.ZookeeperClientManager

@Singleton
class ZookeeperRegistryBuilder private constructor() : RegistryBuilder {

  companion object {
    @JvmField val INSTANCE = ZookeeperRegistryBuilder()
  }

  override fun getRegistry(name: String): Registry? {
    val clientManager = ZookeeperClientManager.INSTANCE
    val client = clientManager.getZookeeperClient(name) ?: return null
    return ZookeeperRegistry(client)
  }
}