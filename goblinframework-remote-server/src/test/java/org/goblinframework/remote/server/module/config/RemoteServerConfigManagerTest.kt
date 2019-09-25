package org.goblinframework.remote.server.module.config

import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class RemoteServerConfigManagerTest {

  @Test
  fun getRemoteServerConfig() {
    val config = RemoteServerConfigManager.INSTANCE.getRemoteServerConfig()
    assertNotNull(config)
  }
}