package org.goblinframework.remote.core.registry

import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class RemoteRegistryManagerTest {

  @Test
  fun getRemoteRegistry() {
    val rm = RemoteRegistryManager.INSTANCE
    val r = rm.getRemoteRegistry()
    assertNotNull(r)
  }
}