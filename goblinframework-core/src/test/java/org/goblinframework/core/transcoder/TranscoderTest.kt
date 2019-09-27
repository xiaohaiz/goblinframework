package org.goblinframework.core.transcoder

import org.apache.commons.io.output.ByteArrayOutputStream
import org.bson.types.ObjectId
import org.goblinframework.api.core.HostAndPort
import org.goblinframework.core.compression.CompressionThreshold
import org.goblinframework.core.compression.CompressorMode
import org.goblinframework.core.container.SpringManagedBean
import org.goblinframework.core.mapper.JsonMapper
import org.goblinframework.core.serialization.SerializerMode
import org.goblinframework.core.util.RandomUtils
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class TranscoderTest : SpringManagedBean() {

  @Test
  fun bs() {
    val bs = RandomUtils.nextBytes(512)
    bs[0] = 0
    val bos = ByteArrayOutputStream()
    TranscoderSetting.builder()
        .build()
        .transcoder().encode(bos, bs)
    bos.toInputStream().use {
      val dr = Transcoder.decode(it)
      assertFalse(dr.magic)
      assertEquals(0.toByte(), dr.serializer)
      assertEquals(0.toByte(), dr.compressor)
      assertFalse(dr.wrapper)
      assertArrayEquals(bs, dr.result as ByteArray)
    }
  }

  @Test
  fun string() {
    val s = RandomUtils.randomAlphanumeric(4096)
    val bos = ByteArrayOutputStream()
    TranscoderSetting.builder()
        .compressor(CompressorMode.GZIP)
        .compressionThreshold(CompressionThreshold._1K)
        .build()
        .transcoder().encode(bos, s)
    bos.toInputStream().use {
      val dr = Transcoder.decode(it)
      assertTrue(dr.magic)
      assertEquals(0.toByte(), dr.serializer)
      assertEquals(CompressorMode.GZIP.id, dr.compressor)
      assertFalse(dr.wrapper)
      assertEquals(s, dr.result)
    }
  }

  @Test
  fun wrapper() {
    val bs = RandomUtils.nextBytes(4096)
    for (mode in SerializerMode.values()) {
      val bos = ByteArrayOutputStream()
      TranscoderSetting.builder()
          .serializer(mode)
          .build()
          .transcoder().encode(bos, ByteArrayWrapper(bs))
      bos.toInputStream().use {
        val dr = Transcoder.decode(it)
        assertTrue(dr.magic)
        assertEquals(mode.id, dr.serializer)
        assertEquals(0.toByte(), dr.compressor)
        assertTrue(dr.wrapper)
        assertArrayEquals(bs, dr.result as ByteArray)
      }
    }
  }

  @Test
  fun json() {
    val s = HostAndPort("127.0.0.1", 8080)
    val bos = ByteArrayOutputStream()
    TranscoderSetting.builder()
        .build()
        .transcoder().encode(bos, s)
    bos.toInputStream().use {
      val dr = Transcoder.decode(it)
      assertTrue(dr.magic)
      assertEquals(0.toByte(), dr.serializer)
      assertEquals(0.toByte(), dr.compressor)
      assertFalse(dr.wrapper)
      println(dr.result)
      assertTrue(dr.result is String)
      assertEquals(s, JsonMapper.asObject(dr.result as String, HostAndPort::class.java))
    }
  }

  @Test
  fun encode() {
    val s = ObjectId()
    for (mode in SerializerMode.values()) {
      val bos = ByteArrayOutputStream()
      TranscoderSetting.builder()
          .serializer(mode)
          .build()
          .transcoder().encode(bos, s)
      bos.toInputStream().use {
        val dr = Transcoder.decode(it)
        assertTrue(dr.magic)
        assertEquals(mode.id, dr.serializer)
        assertEquals(0.toByte(), dr.compressor)
        assertFalse(dr.wrapper)
        assertEquals(s, dr.result)
      }
    }
  }
}