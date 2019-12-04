package org.goblinframework.remote.server.transport

import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class RemoteTransportServerManagerTest {

  @Test
  fun getRemoteTransportServer() {
    val tsm = RemoteTransportServerManager.INSTANCE
    val ts = tsm.getRemoteTransportServer()
    assertNotNull(ts)
  }
}