package org.goblinframework.core.util

import org.bson.types.ObjectId
import org.junit.Test

class JsonUtilsTest {

  @Test
  fun objectId() {
    val source = ObjectId()
    val s = JsonUtils.toJson(source)
    println(s)
  }
}