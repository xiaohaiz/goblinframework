package org.goblinframework.core.mapper

import org.bson.types.ObjectId
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class YamlMapperTest {

  @Test
  fun objectId() {
    val source = ObjectId()
    val s = YamlMapper.toJson(source)
    val target = YamlMapper.asObject(s, ObjectId::class.java)
    assertEquals(source, target)
  }

  @Test
  fun instant() {
    val source = Instant.now()
    val s = YamlMapper.toJson(source)
    val target = YamlMapper.asObject(s, Instant::class.java)
    assertEquals(source, target)
  }
}