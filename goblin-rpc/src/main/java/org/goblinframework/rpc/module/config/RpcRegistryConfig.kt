package org.goblinframework.rpc.module.config

import org.goblinframework.api.core.SerializerMode
import org.goblinframework.core.config.GoblinConfig
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@GoblinManagedBean("Remote")
class RpcRegistryConfig internal constructor(internal val mapper: RpcRegistryConfigMapper)
  : GoblinManagedObject(), GoblinConfig, RpcRegistryConfigMXBean {

  override fun getZookeeper(): String {
    return mapper.zookeeper!!
  }

  override fun getConnectionTimeout(): Int {
    return mapper.connectionTimeout!!
  }

  override fun getSessionTimeout(): Int {
    return mapper.sessionTimeout!!
  }

  override fun getSerializer(): SerializerMode {
    return mapper.serializer!!
  }
}