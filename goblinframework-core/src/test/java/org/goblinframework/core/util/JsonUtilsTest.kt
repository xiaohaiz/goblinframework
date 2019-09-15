package org.goblinframework.core.util

import org.bson.types.ObjectId
import org.junit.Assert
import org.junit.Test

class JsonUtilsTest {

  @Test
  fun objectId() {
    val source = ObjectId()
    val s = JsonUtils.toJson(source)
    val target = JsonUtils.asObject(s, ObjectId::class.java)
    Assert.assertEquals(source, target)
  }
}