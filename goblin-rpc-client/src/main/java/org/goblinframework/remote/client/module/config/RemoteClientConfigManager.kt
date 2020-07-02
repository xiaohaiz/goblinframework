package org.goblinframework.remote.client.module.config

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@Singleton
@GoblinManagedBean("RemoteClient")
class RemoteClientConfigManager private constructor()
  : GoblinManagedObject(), RemoteClientConfigManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteClientConfigManager()
  }

  private val configParser = RemoteClientConfigParser()

  override fun initializeBean() {
    configParser.initialize()
  }

  override fun getRemoteClientConfig(): RemoteClientConfig {
    return configParser.getFromBuffer(RemoteClientConfigParser.CONFIG_NAME)!!
  }

  override fun disposeBean() {
    configParser.dispose()
  }
}