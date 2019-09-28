package org.goblinframework.remote.client.module.config

import org.goblinframework.core.config.GoblinConfig
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.registry.core.RegistryLocation

@GoblinManagedBean(type = "RemoteClient")
class RemoteClientConfig(val mapper: RemoteClientConfigMapper)
  : GoblinManagedObject(), GoblinConfig, RemoteClientConfigMXBean {

  var registryLocation: RegistryLocation? = null

  override fun getRegistry(): String? {
    return registryLocation?.toString()
  }
}