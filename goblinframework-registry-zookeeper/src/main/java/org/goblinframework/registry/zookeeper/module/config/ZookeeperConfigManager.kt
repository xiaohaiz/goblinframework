package org.goblinframework.registry.zookeeper.module.config

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@GoblinManagedBean(type = "registry.zookeeper")
class ZookeeperConfigManager : GoblinManagedObject(), ZookeeperConfigManagerMXBean {

  companion object {
    @JvmField val INSTANCE = ZookeeperConfigManager()
  }

  val configParser = ZookeeperConfigParser()

  fun getZookeeperConfig(name: String): ZookeeperConfig? {
    return configParser.getFromBuffer(name)
  }

  override fun disposeBean() {
    configParser.dispose()
  }
}