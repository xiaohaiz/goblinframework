package org.goblinframework.remote.core.module.config

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@Singleton
@GoblinManagedBean("Remote")
class RemoteRegistryConfigManager private constructor()
  : GoblinManagedObject(), RemoteRegistryConfigManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteRegistryConfigManager()
  }

  private val parser = RemoteRegistryConfigParser()

  override fun initializeBean() {
    parser.initialize()
  }

  override fun getRemoteRegistryConfig(): RemoteRegistryConfig? {
    return parser.getFromBuffer(RemoteRegistryConfigParser.CONFIG_NAME)
  }

  override fun disposeBean() {
    parser.dispose()
  }
}