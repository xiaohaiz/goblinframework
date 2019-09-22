package org.goblinframework.registry.zookeeper.module.config

import org.goblinframework.core.config.Config
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.serialization.SerializerMode

@GoblinManagedBean("REGISTRY.ZOOKEEPER")
class ZookeeperConfig
internal constructor(val mapper: ZookeeperConfigMapper)
  : GoblinManagedObject(), ZookeeperConfigMXBean, Config {

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
