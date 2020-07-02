package org.goblinframework.registry.zookeeper

import java.lang.management.PlatformManagedObject

interface ZookeeperClientMXBean : PlatformManagedObject {

  fun getTranscoder(): ZkTranscoderMXBean

}