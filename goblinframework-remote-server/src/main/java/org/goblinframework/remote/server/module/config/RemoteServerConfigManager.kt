package org.goblinframework.remote.server.module.config

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.core.GoblinManagedObject

@Singleton
class RemoteServerConfigManager private constructor()
  : GoblinManagedObject(), RemoteServerConfigManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteServerConfigManager()
  }

  val configParser = RemoteServerConfigParser()

  fun getRemoteServerConfig(): RemoteServerConfig? {
    return configParser.getRemoteServerConfig()
  }

  override fun disposeBean() {
    configParser.dispose()
  }
}