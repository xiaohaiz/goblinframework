package org.goblinframework.core.transcoder

import org.bson.types.ObjectId
import org.goblinframework.core.container.SpringManagedBean
import org.goblinframework.core.serialization.SerializerManager
import org.goblinframework.core.serialization.SerializerMode
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertEquals
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
    Transcoder1.encode(bos, source, serializer)
    val bis = ByteArrayInputStream(bos.toByteArray())
    bos.close()
    val obj = Transcoder1.decode(bis)
    assertEquals(serializer.mode().id, obj.serializer)
    assertEquals(source, obj.decoded)
    bis.close()
  }
}