package org.goblinframework.registry.zookeeper.module.config

import org.goblinframework.api.config.GoblinConfig
import org.goblinframework.api.core.GoblinManagedBean
import org.goblinframework.api.core.GoblinManagedObject
import org.goblinframework.core.serialization.SerializerMode

@GoblinManagedBean(type = "registry.zookeeper")
class ZookeeperConfig
internal constructor(val mapper: ZookeeperConfigMapper)
  : GoblinManagedObject(), ZookeeperConfigMXBean, GoblinConfig {

  override fun getName(): String {
    return mapper.name!!
  }

  override fun getServers(): String {
    return mapper.servers!!
  }

  override fun getSerializer(): SerializerMode {
    return mapper.serializer!!
  }

}
