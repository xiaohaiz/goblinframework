package org.goblinframework.remote.core.module.config

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
    val cm = RemoteRegistryConfigManager.INSTANCE
    val c = cm.getRemoteRegistryConfig()
    assertNotNull(c)
  }
}