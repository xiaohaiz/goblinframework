package org.goblinframework.remote.core.registry

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.registry.zookeeper.ZookeeperClient
import org.goblinframework.registry.zookeeper.ZookeeperRegistryFactory

@GoblinManagedBean("Remote")
class RemoteRegistry internal constructor(private val client: ZookeeperClient)
  : GoblinManagedObject(), RemoteRegistryMXBean {

  private val registry = ZookeeperRegistryFactory.createZookeeperRegistry(client)

  override fun disposeBean() {
    registry.dispose()
    client.dispose()
  }
}