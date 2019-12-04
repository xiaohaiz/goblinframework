package org.goblinframework.registry.zookeeper

import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class ZookeeperClientTest {

  @Test
  fun zookeeperClient() {
    val client = ZookeeperClientSetting.builder()
        .addresses("127.0.0.1")
        .build()
        .createZookeeperClient()
    client.dispose()
  }
}