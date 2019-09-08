package org.goblinframework.serialization.hessian.provider

import org.apache.commons.lang3.RandomUtils
import org.apache.commons.lang3.SystemUtils
import org.bson.types.ObjectId
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
import java.math.BigDecimal
import java.time.Instant
import javax.management.ObjectName

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class Hessian2SerializerTest {

  @Test
  fun testObjectId() {
    val s = SerializerManager.INSTANCE.getSerializer(Serializer.HESSIAN2)!!
    val source = ObjectId()
    ByteArrayOutputStream().use {
      s.serialize(source, it)
      ByteArrayInputStream(it.toByteArray())
    }.use {
      val target = s.deserialize(it)
      target as ObjectId
      Assert.assertEquals(source, target)
    }
    val bs = s.serialize(source)
    ByteArrayInputStream(bs).use {
      val target = s.deserialize(it)
      target as ObjectId
      Assert.assertEquals(source, target)
    }
    val target = s.deserialize(bs)
    target as ObjectId
    Assert.assertEquals(source, target)
  }

  @Test
  fun testBigDecimal() {
    val s = SerializerManager.INSTANCE.getSerializer(Serializer.HESSIAN2)!!
    val source = BigDecimal.valueOf(RandomUtils.nextDouble())
    ByteArrayOutputStream().use {
      s.serialize(source, it)
      ByteArrayInputStream(it.toByteArray())
    }.use {
      val target = s.deserialize(it)
      target as BigDecimal
      Assert.assertEquals(source, target)
    }
    val bs = s.serialize(source)
    ByteArrayInputStream(bs).use {
      val target = s.deserialize(it)
      target as BigDecimal
      Assert.assertEquals(source, target)
    }
    val target = s.deserialize(bs)
    target as BigDecimal
    Assert.assertEquals(source, target)
  }

  @Test
  fun testFile() {
    val s = SerializerManager.INSTANCE.getSerializer(Serializer.HESSIAN2)!!
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
  fun testObjectName() {
    val s = SerializerManager.INSTANCE.getSerializer(Serializer.HESSIAN2)!!
    val source = ObjectName("org.goblinframework:type=TEST,name=testObjectName")
    ByteArrayOutputStream().use {
      s.serialize(source, it)
      ByteArrayInputStream(it.toByteArray())
    }.use {
      val target = s.deserialize(it)
      target as ObjectName
      Assert.assertEquals(source, target)
    }
    val bs = s.serialize(source)
    ByteArrayInputStream(bs).use {
      val target = s.deserialize(it)
      target as ObjectName
      Assert.assertEquals(source, target)
    }
    val target = s.deserialize(bs)
    target as ObjectName
    Assert.assertEquals(source, target)
  }

  @Test
  fun serializeInstant() {
    val s = SerializerManager.INSTANCE.getSerializer(Serializer.HESSIAN2)!!
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