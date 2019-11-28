package org.goblinframework.registry.module.monitor

import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class ZkConnectionMethodPoolTest {
  @Test
  fun triggerInitialization() {
    ZkConnectionMethodPool.triggerInitialization()
  }
}