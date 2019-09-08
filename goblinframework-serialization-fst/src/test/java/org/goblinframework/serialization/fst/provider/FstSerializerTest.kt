package org.goblinframework.serialization.fst.provider

import org.apache.commons.lang3.SystemUtils
import org.goblinframework.serialization.core.Serializer
import org.goblinframework.serialization.core.manager.SerializerManager
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.Instant

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class FstSerializerTest {

  @Test
  fun testFile() {
    val s = SerializerManager.INSTANCE.getSerializer(Serializer.FST)!!
    val source = SystemUtils.getJavaIoTmpDir()
    ByteArrayOutputStream().use {
      s.serialize(source, it)
      ByteArrayInputStream(it.toByteArray())
    }.use {
      val target = s.deserialize(it)
      target as File
      Assert.assertEquals(source.path, target.path)
    }
    val bs = s.serialize(source)
    ByteArrayInputStream(bs).use {
      val target = s.deserialize(it)
      target as File
      Assert.assertEquals(source.path, target.path)
    }
    val target = s.deserialize(bs)
    target as File
    Assert.assertEquals(source.path, target.path)
  }

  @Test
  fun serializeInstant() {
    val s = SerializerManager.INSTANCE.getSerializer(Serializer.FST)!!
    val source = Instant.now()
    ByteArrayOutputStream().use {
      s.serialize(source, it)
      ByteArrayInputStream(it.toByteArray())
    }.use {
      val target = s.deserialize(it)
      target as Instant
      Assert.assertEquals(source.toEpochMilli(), target.toEpochMilli())
    }
    val bs = s.serialize(source)
    ByteArrayInputStream(bs).use {
      val target = s.deserialize(it)
      target as Instant
      Assert.assertEquals(source.toEpochMilli(), target.toEpochMilli())
    }
    val target = s.deserialize(bs)
    target as Instant
    Assert.assertEquals(source.toEpochMilli(), target.toEpochMilli())
  }
}