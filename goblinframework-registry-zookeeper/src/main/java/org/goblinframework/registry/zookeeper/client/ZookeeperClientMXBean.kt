package org.goblinframework.registry.zookeeper.client

import java.lang.management.PlatformManagedObject

interface ZookeeperClientMXBean : PlatformManagedObject {

  fun getTranscoder(): ZkTranscoderMXBean

}