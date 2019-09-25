package org.goblinframework.remote.server.module.config

import org.goblinframework.api.config.GoblinConfig
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject

@GoblinManagedBean(type = "RemoteServer")
class RemoteServerConfig(val mapper: RemoteServerConfigMapper)
  : GoblinManagedObject(), GoblinConfig, RemoteServerConfigMXBean {

  override fun getHost(): String {
    return mapper.host!!
  }

  override fun getPort(): Int {
    return mapper.port!!
  }
}