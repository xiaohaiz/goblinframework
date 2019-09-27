package org.goblinframework.core.transcoder

import org.apache.commons.lang3.RandomStringUtils
import org.bson.types.ObjectId
import org.goblinframework.core.compression.CompressorManager
import org.goblinframework.core.compression.CompressorMode
import org.goblinframework.core.container.SpringManagedBean
import org.goblinframework.core.serialization.SerializerManager
import org.goblinframework.core.serialization.SerializerMode
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class TranscoderTest : SpringManagedBean() {

  @Test
  fun transcode() {
    val serializer = SerializerManager.INSTANCE.getSerializer(SerializerMode.FST)
    val source = ObjectId()
    val bos = ByteArrayOutputStream()
    TranscoderSetting.builder()
        .serializer(serializer)
        .build()
        .transcoder()
        .encode(bos, source)
    val bis = ByteArrayInputStream(bos.toByteArray())
    bos.close()
    val obj = Transcoder.decode(bis)
    assertEquals(serializer.mode().id, obj.serializer)
    assertEquals(source, obj.result)
    assertTrue(obj.magic)
    bis.close()
  }

  @Test
  fun encode() {
    val compressor = CompressorManager.INSTANCE.getCompressor(CompressorMode.BZIP2)
    val serializer = SerializerManager.INSTANCE.getSerializer(SerializerMode.HESSIAN2)
    val transcoder = TranscoderSetting.builder()
        .compressor(compressor)
        .serializer(serializer)
        .build()
        .transcoder()

    val s = RandomStringUtils.randomAlphanumeric(4096)
    val bos = ByteArrayOutputStream(512)
    transcoder.encode(bos, s)
    val bis = ByteArrayInputStream(bos.toByteArray())
    val dr = Transcoder.decode(bis)
    assertEquals(s, dr.result)
  }
}