package org.goblinframework.registry.zookeeper.provider

import org.goblinframework.api.registry.Registry
import org.goblinframework.api.registry.RegistryLocation
import org.goblinframework.api.registry.RegistrySystem
import org.goblinframework.registry.zookeeper.client.ZookeeperClient

class ZookeeperRegistry(private val client: ZookeeperClient) : Registry {

  private val location = RegistryLocation(RegistrySystem.ZKP, client.config.getName())

  override fun location(): RegistryLocation {
    return location
  }
}