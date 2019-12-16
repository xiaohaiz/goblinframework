package org.goblinframework.rpc.module.config

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@Singleton
@GoblinManagedBean("Remote")
class RpcRegistryConfigManager private constructor()
  : GoblinManagedObject(), RpcRegistryConfigManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RpcRegistryConfigManager()
  }

  private val parser = RpcRegistryConfigParser()

  override fun initializeBean() {
    parser.initialize()
  }

  override fun getRpcRegistryConfig(): RpcRegistryConfig? {
    return parser.getFromBuffer(RpcRegistryConfigParser.CONFIG_NAME)
  }

  override fun disposeBean() {
    parser.dispose()
  }
}