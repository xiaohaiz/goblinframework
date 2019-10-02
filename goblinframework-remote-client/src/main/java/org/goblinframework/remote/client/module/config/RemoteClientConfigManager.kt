package org.goblinframework.remote.client.module.config

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedObject

@Singleton
class RemoteClientConfigManager private constructor()
  : GoblinManagedObject(), RemoteClientConfigManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteClientConfigManager()
  }

  private val configParser = RemoteClientConfigParser()

  init {
    configParser.initialize()
  }

  fun getRemoteClientConfig(): RemoteClientConfig? {
    return configParser.getRemoteClientConfig()
  }

  override fun disposeBean() {
    configParser.dispose()
  }
}