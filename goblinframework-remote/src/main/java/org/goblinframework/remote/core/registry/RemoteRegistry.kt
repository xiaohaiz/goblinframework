package org.goblinframework.remote.core.registry

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.registry.zookeeper.ZookeeperClient
import org.goblinframework.registry.zookeeper.ZookeeperRegistryFactory
import org.goblinframework.registry.zookeeper.ZookeeperRegistryPathKeeper
import org.goblinframework.registry.zookeeper.ZookeeperRegistryPathWatcher

@GoblinManagedBean("Remote")
class RemoteRegistry internal constructor(private val client: ZookeeperClient)
  : GoblinManagedObject(), RemoteRegistryMXBean {

  private val registry = ZookeeperRegistryFactory.createZookeeperRegistry(client)

  fun getChildren(path: String): List<String> {
    return registry.getChildren(path)
  }

  fun createKeeper(): ZookeeperRegistryPathKeeper {
    return registry.createKeeper()
  }

  fun createWatcher(): ZookeeperRegistryPathWatcher {
    return registry.createWatcher()
  }

  override fun disposeBean() {
    registry.dispose()
    client.dispose()
  }
}