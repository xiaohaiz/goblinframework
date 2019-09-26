package org.goblinframework.remote.client.module.config

import org.goblinframework.api.config.GoblinConfig
import org.goblinframework.api.registry.RegistryLocation
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject

@GoblinManagedBean(type = "RemoteClient")
class RemoteClientConfig(val mapper: RemoteClientConfigMapper)
  : GoblinManagedObject(), GoblinConfig, RemoteClientConfigMXBean {

  var registryLocation: RegistryLocation? = null

  override fun getRegistry(): String? {
    return registryLocation?.toString()
  }
}