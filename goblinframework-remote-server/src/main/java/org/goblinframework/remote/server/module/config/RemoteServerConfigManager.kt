package org.goblinframework.remote.server.module.config

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@Singleton
@GoblinManagedBean("RemoteServer")
class RemoteServerConfigManager private constructor()
  : GoblinManagedObject(), RemoteServerConfigManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteServerConfigManager()
  }

  private val parser = RemoteServerConfigParser()

  override fun initializeBean() {
    parser.initialize()
  }

  override fun getRemoteServerConfig(): RemoteServerConfig {
    return parser.getFromBuffer(RemoteServerConfigParser.CONFIG_NAME)!!
  }

  override fun disposeBean() {
    parser.dispose()
  }
}