package org.goblinframework.remote.core.module.config

import org.goblinframework.rpc.module.config.RpcRegistryConfigManager
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class RemoteRegistryConfigManagerTest {

  @Test
  fun getRemoteRegistryConfig() {
    val cm = RpcRegistryConfigManager.INSTANCE
    val c = cm.getRpcRegistryConfig()
    assertNotNull(c)
  }
}