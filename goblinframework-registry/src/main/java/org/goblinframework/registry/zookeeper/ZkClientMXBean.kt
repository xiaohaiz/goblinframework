package org.goblinframework.registry.zookeeper

import java.lang.management.PlatformManagedObject

interface ZkClientMXBean : PlatformManagedObject {

  fun getTranscoder(): ZkTranscoderMXBean

}