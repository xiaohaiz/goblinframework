package org.goblinframework.registry.zookeeper.module.config

import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class ZookeeperConfigManagerTest {

  @Test
  fun getZookeeperConfig() {
    val configManager = ZookeeperConfigManager.INSTANCE
    val config = configManager.getZookeeperConfig("_ut")
    assertNotNull(config)
  }
}