package org.goblinframework.core.transcoder

import org.bson.types.ObjectId
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
    val obj = TranscoderUtils.decode(bis)
    assertEquals(serializer.mode().id, obj.serializer)
    assertEquals(source, obj.result)
    assertTrue(obj.magic)
    bis.close()
  }
}