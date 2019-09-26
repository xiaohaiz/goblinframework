package org.goblinframework.remote.server.module.config

import org.goblinframework.api.config.GoblinConfig
import org.goblinframework.api.registry.RegistryLocation
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject

@GoblinManagedBean(type = "RemoteServer")
class RemoteServerConfig(val mapper: RemoteServerConfigMapper)
  : GoblinManagedObject(), GoblinConfig, RemoteServerConfigMXBean {

  var registryLocation: RegistryLocation? = null

  override fun getName(): String {
    return mapper.name!!
  }

  override fun getHost(): String {
    return mapper.host!!
  }

  override fun getPort(): Int {
    return mapper.port!!
  }

  override fun getRegistry(): String? {
    return registryLocation?.toString()
  }
}