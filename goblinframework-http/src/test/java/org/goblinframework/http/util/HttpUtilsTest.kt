package org.goblinframework.http.util

import org.goblinframework.core.util.HttpUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class HttpUtilsTest {

  @Test
  fun buildQueryString() {
    val m = mutableMapOf<String, Any>()
    m["a"] = listOf(1, 2, 3)
    m["b"] = arrayOf(4, 5, 6)
    m["c"] = IntArray(3).also {
      it[0] = 7
      it[1] = 8
      it[2] = 9
    }
    val qs = HttpUtils.buildQueryString(m)
    assertEquals("a=1&a=2&a=3&b=4&b=5&b=6&c=7&c=8&c=9", qs)
  }
}