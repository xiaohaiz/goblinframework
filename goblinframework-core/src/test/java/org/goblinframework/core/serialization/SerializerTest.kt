package org.goblinframework.core.serialization

import org.bson.types.ObjectId
import org.goblinframework.core.container.SpringContainerObject
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.util.*

abstract class SerializerTest : SpringContainerObject() {

  abstract fun serializer(): Serializer

  @Test
  fun objectId() {
    val serializer = serializer()
    val before = ObjectId()
    val bs = serializer.serialize(before)
    logger.debug("${ObjectId::class.java.name} serialized: ${bs.size}")
    val after = serializer.deserialize(bs) as ObjectId
    assertEquals(before, after)
  }

  @Test
  fun instant() {
    val serializer = serializer()
    val before = Instant.now()
    val bs = serializer.serialize(before)
    logger.debug("${Instant::class.java.name} serialized: ${bs.size}")
    val after = serializer.deserialize(bs) as Instant
    assertEquals(before.toEpochMilli(), after.toEpochMilli())
  }

  @Test
  fun calendar() {
    val serializer = serializer()
    val before = Calendar.getInstance()
    val bs = serializer.serialize(before)
    logger.debug("${Calendar::class.java.name} serialized: ${bs.size}")
    val after = serializer.deserialize(bs) as Calendar
    assertEquals(before.timeInMillis, after.timeInMillis)
  }
}