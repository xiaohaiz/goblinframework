package org.goblinframework.registry.zookeeper.provider

import org.bson.types.ObjectId
import org.goblinframework.core.util.RandomUtils
import org.goblinframework.registry.core.RegistrySystem
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

  @Test
  fun data() {
    val zr = RegistrySystem.ZKP.getRegistry("_ut")!!
    val rn = RandomUtils.nextObjectId()
    try {
      zr.createEphemeral("/$rn", "HELLO".toByteArray(Charsets.UTF_8))
      var s = zr.readData<ByteArray>("/$rn")?.toString(Charsets.UTF_8)
      assertEquals("HELLO", s)
      zr.writeData("/$rn", "WORLD".toByteArray(Charsets.UTF_8))
      s = zr.readData<ByteArray>("/$rn")?.toString(Charsets.UTF_8)
      assertEquals("WORLD", s)

      zr.writeData("/$rn", "HELLO WORLD")
      s = zr.readData<String>("/$rn")
      assertEquals("HELLO WORLD", s)

      val id = ObjectId()
      zr.writeData("/$rn", id)
      val di = zr.readData<ObjectId>("/$rn")
      assertEquals(id, di)
    } finally {
      zr.deleteRecursive("/$rn")
    }
  }
}