package org.goblinframework.remote.core.module.config

import org.goblinframework.core.config.GoblinConfig
import org.goblinframework.core.serialization.SerializerMode
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@GoblinManagedBean("RemoteCore")
class RemoteRegistryConfig internal constructor(private val mapper: RemoteRegistryConfigMapper)
  : GoblinManagedObject(), GoblinConfig, RemoteRegistryConfigMXBean {

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