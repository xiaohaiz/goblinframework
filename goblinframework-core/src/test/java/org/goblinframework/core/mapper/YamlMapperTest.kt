package org.goblinframework.core.mapper

import org.bson.types.ObjectId
import org.junit.Assert.assertEquals
import org.junit.Test

class YamlMapperTest {

  @Test
  fun objectId() {
    val source = ObjectId()
    val s = YamlMapper.toJson(source)
    val target = YamlMapper.asObject(s, ObjectId::class.java)
    assertEquals(source, target)
  }
}