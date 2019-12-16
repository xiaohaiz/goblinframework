package org.goblinframework.rpc.registry

import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class RpcRegistryManagerTest {

  @Test
  fun getRpcRegistry() {
    val rm = RpcRegistryManager.INSTANCE
    val r = rm.getRpcRegistry()
    assertNotNull(r)
  }
}