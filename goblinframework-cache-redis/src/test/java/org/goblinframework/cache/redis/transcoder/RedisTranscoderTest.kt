package org.goblinframework.cache.redis.transcoder

import org.apache.commons.lang3.RandomStringUtils
import org.bson.types.ObjectId
import org.goblinframework.api.core.SerializerMode
import org.goblinframework.core.serialization.SerializerManager
import org.goblinframework.core.util.RandomUtils
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class RedisTranscoderTest {

  @Test
  fun redisTranscoder() {
    val serializer = SerializerManager.INSTANCE.getSerializer(SerializerMode.HESSIAN2)
    val transcoder = RedisTranscoder(serializer, null, null)
    try {
      val key = RandomStringUtils.randomAlphabetic(32)
      var kb = transcoder.encodeKey(key)
      assertEquals(key, transcoder.decodeKey(kb))

      val value = RandomUtils.randomAlphanumeric(4096)
      kb = transcoder.encodeValue(value)
      assertEquals(value, transcoder.decodeValue(kb))

      val id = ObjectId()
      kb = transcoder.encodeValue(id)
      assertEquals(id, transcoder.decodeValue(kb))

      val bs = RandomUtils.nextBytes(512)
      kb = transcoder.encodeValue(bs)
      assertArrayEquals(bs, transcoder.decodeValue(kb) as ByteArray)
    } finally {
      transcoder.dispose()
    }
  }
}