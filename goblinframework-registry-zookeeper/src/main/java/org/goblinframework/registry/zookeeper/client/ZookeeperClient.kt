package org.goblinframework.registry.zookeeper.client

import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject

@GoblinManagedBean(type = "registry.zookeeper")
class ZookeeperClient : GoblinManagedObject(), ZookeeperClientMXBean {
}