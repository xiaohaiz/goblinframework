package org.goblinframework.transport.core.codec

import org.apache.commons.lang3.RandomStringUtils
import org.goblinframework.core.compression.CompressorManager
import org.goblinframework.core.compression.CompressorMode
import org.goblinframework.core.serialization.SerializerManager
import org.goblinframework.core.serialization.SerializerMode
import org.goblinframework.core.transcoder.TranscoderUtils
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
class TranscoderImplTest {

  @Test
  fun encode() {
    val compressor = CompressorManager.INSTANCE.getCompressor(CompressorMode.BZIP2)
    val serializer = SerializerManager.INSTANCE.getSerializer(SerializerMode.HESSIAN2)
    val transcoder = TranscoderUtils.encoder()
        .compressor(compressor)
        .serializer(serializer)
        .buildTranscoder()
    assertTrue(transcoder is Transcoder1Impl)

    val s = RandomStringUtils.randomAlphanumeric(4096)
    val bos = ByteArrayOutputStream(512)
    transcoder.encode(bos, s)
    val bis = ByteArrayInputStream(bos.toByteArray())
    val dr = transcoder.decode(bis)
    assertEquals(s, dr.result)
  }
}