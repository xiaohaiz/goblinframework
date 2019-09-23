package org.goblinframework.registry.zookeeper.client

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.service.GoblinManagedObject

@Singleton
class ZookeeperClientManager private constructor()
  : GoblinManagedObject(), ZookeeperClientManagerMXBean {

  companion object {
    @JvmField val INSTANCE = ZookeeperClientManager()
  }
}