package org.goblinframework.remote.server.module.config

import org.goblinframework.core.config.GoblinConfig
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.registry.core.RegistryLocation

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