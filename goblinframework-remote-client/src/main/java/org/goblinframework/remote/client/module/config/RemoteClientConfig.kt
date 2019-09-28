package org.goblinframework.remote.client.module.config

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.api.registry.RegistryLocation
import org.goblinframework.core.config.GoblinConfig

@GoblinManagedBean(type = "RemoteClient")
class RemoteClientConfig(val mapper: RemoteClientConfigMapper)
  : GoblinManagedObject(), GoblinConfig, RemoteClientConfigMXBean {

  var registryLocation: RegistryLocation? = null

  override fun getRegistry(): String? {
    return registryLocation?.toString()
  }
}