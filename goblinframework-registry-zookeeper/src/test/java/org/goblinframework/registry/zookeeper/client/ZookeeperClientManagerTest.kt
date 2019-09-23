package org.goblinframework.registry.zookeeper.client

import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class ZookeeperClientManagerTest {

  @Test
  fun getZookeeperClient() {
    val clientManager = ZookeeperClientManager.INSTANCE
    val client = clientManager.getZookeeperClient("_ut")
    assertNotNull(client)
  }
}