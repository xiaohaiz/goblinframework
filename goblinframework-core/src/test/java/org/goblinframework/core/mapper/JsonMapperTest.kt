package org.goblinframework.core.mapper

import org.bson.types.ObjectId
import org.goblinframework.core.util.JsonUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class JsonMapperTest {

  @Test
  fun objectId() {
    val source = ObjectId()
    val s = JsonUtils.toJson(source)
    val target = JsonUtils.asObject(s, ObjectId::class.java)
    assertEquals(source, target)
  }
}