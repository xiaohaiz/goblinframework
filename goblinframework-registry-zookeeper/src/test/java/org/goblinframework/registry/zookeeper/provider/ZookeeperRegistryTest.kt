package org.goblinframework.registry.zookeeper.provider

import org.goblinframework.api.registry.RegistrySystem
import org.goblinframework.core.util.RandomUtils
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class ZookeeperRegistryTest {

  @Test
  fun children() {
    val zr = RegistrySystem.ZKP.getRegistry("_ut")!!
    val rn = RandomUtils.nextObjectId()
    try {
      zr.createEphemeral("/$rn/1")
      zr.createEphemeral("/$rn/2")
      zr.createEphemeral("/$rn/3")
      val children = zr.getChildren("/$rn")
      assertEquals(3, children.size)
      assertTrue(children.contains("1"))
      assertTrue(children.contains("2"))
      assertTrue(children.contains("3"))
    } finally {
      zr.deleteRecursive("/$rn")
    }
  }
}