package org.goblinframework.registry.zookeeper.client

import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.registry.zookeeper.module.config.ZookeeperConfig

@GoblinManagedBean(type = "registry.zookeeper")
class ZookeeperClient
internal constructor(private val config: ZookeeperConfig)
  : GoblinManagedObject(), ZookeeperClientMXBean {
}