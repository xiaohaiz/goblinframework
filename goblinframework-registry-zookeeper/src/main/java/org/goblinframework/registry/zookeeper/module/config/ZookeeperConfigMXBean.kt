package org.goblinframework.registry.zookeeper.module.config

import org.goblinframework.core.serialization.SerializerMode
import java.lang.management.PlatformManagedObject

interface ZookeeperConfigMXBean : PlatformManagedObject {

  fun getName(): String

  fun getServers(): String

  fun getSerializer(): SerializerMode

}