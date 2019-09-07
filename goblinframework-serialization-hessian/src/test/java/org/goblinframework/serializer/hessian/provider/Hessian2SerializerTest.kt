package org.goblinframework.serializer.hessian.provider

import org.goblinframework.api.serialization.Serializer0
import org.goblinframework.serialization.core.SerializationManager
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.Instant

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class Hessian2SerializerTest {

  @Test
  fun testInstant() {
    val i = Instant.now()
    val t = i.toEpochMilli()
    val s = SerializationManager.INSTANCE.getSerialization(Serializer0.HESSIAN2)!!
    val bos = ByteArrayOutputStream()
    s.serialize(i, bos)
    val bis = ByteArrayInputStream(bos.toByteArray())
    bos.close()
    val d = s.deserialize(bis) as Instant
    bis.close()
    Assert.assertEquals(t, d.toEpochMilli())
  }
}