package org.goblinframework.registry.zookeeper

import org.junit.Test

class ZkConnectionMethodPoolTest {
  @Test
  fun triggerInitialization() {
    ZkConnectionMethodPool.triggerInitialization()
  }
}