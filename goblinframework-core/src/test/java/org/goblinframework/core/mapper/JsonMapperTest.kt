package org.goblinframework.core.mapper

import org.bson.types.ObjectId
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class JsonMapperTest {

  @Test
  fun objectId() {
    val source = ObjectId()
    val s = JsonMapper.toJson(source)
    val target = JsonMapper.asObject(s, ObjectId::class.java)
    assertEquals(source, target)
  }

  @Test
  fun instant() {
    val source = Instant.now()
    val s = JsonMapper.toJson(source)
    val target = JsonMapper.asObject(s, Instant::class.java)
    assertEquals(source, target)
  }
}