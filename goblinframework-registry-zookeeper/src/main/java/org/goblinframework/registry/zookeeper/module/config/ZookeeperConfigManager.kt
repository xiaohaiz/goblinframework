package org.goblinframework.registry.zookeeper.module.config

import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject

@GoblinManagedBean(type = "registry.zookeeper")
class ZookeeperConfigManager : GoblinManagedObject(), ZookeeperConfigManagerMXBean {

  companion object {
    @JvmField val INSTANCE = ZookeeperConfigManager()
  }

  private val configParser = ZookeeperConfigParser()

  init {
    configParser.initialize()
  }

  fun getZookeeperConfig(name: String): ZookeeperConfig? {
    return configParser.getFromBuffer(name)
  }

  override fun disposeBean() {
    configParser.dispose()
  }
}