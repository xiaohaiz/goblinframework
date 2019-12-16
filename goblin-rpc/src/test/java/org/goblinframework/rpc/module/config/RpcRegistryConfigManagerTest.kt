package org.goblinframework.rpc.module.config

import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class RpcRegistryConfigManagerTest {

  @Test
  fun getRpcRegistryConfig() {
    val cm = RpcRegistryConfigManager.INSTANCE
    val c = cm.getRpcRegistryConfig()
    assertNotNull(c)
  }
}