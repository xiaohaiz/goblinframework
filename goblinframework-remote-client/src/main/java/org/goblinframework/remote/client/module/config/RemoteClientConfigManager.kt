package org.goblinframework.remote.client.module.config

import org.goblinframework.api.core.GoblinManagedObject
import org.goblinframework.api.core.Singleton

@Singleton
class RemoteClientConfigManager private constructor()
  : GoblinManagedObject(), RemoteClientConfigManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteClientConfigManager()
  }

  val configParser = RemoteClientConfigParser()

  fun getRemoteClientConfig(): RemoteClientConfig? {
    return configParser.getRemoteClientConfig()
  }

  override fun disposeBean() {
    configParser.dispose()
  }
}