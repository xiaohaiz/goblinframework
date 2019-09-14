package org.goblinframework.registry.zookeeper.client

import org.apache.commons.lang3.RandomStringUtils
import org.goblinframework.core.serialization.SerializerMode
import org.goblinframework.core.util.RandomUtils
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import java.time.Instant

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class ZkTranscoderManagerTest {

  @Test
  fun transcoder() {
    SerializerMode.values().forEach {
      val transcoder = ZkTranscoderManager.INSTANCE.getTranscoder(it)
      transcoder(transcoder)
    }
  }

  private fun transcoder(transcoder: ZkTranscoder) {
    val bs = RandomUtils.nextBytes(512)
    var obj = transcoder.deserialize(transcoder.serialize(bs))
    assertArrayEquals(bs, obj as ByteArray)

    val s = RandomStringUtils.randomAlphanumeric(1024)
    obj = transcoder.deserialize(transcoder.serialize(s))
    assertEquals(s, obj as String)

    val i = Instant.now()
    obj = transcoder.deserialize(transcoder.serialize(i))
    assertEquals(i, obj as Instant)
  }
}