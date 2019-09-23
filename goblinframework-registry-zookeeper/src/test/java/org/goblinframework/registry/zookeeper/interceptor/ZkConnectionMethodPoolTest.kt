package org.goblinframework.registry.zookeeper.interceptor

import org.junit.Test

class ZkConnectionMethodPoolTest {

  @Test
  fun triggerInitialization() {
    ZkConnectionMethodPool.triggerInitialization()
  }
}