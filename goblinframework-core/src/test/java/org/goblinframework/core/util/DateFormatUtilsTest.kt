package org.goblinframework.core.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.util.*

class DateFormatUtilsTest {

  @Test
  fun parse() {
    val date = DateFormatUtils.parse("2019-09-09 22:20:00")
    assertNotNull(date)
    val time = System.currentTimeMillis()
    val s = DateFormatUtils.format(Date(time))
    assertEquals(time, DateFormatUtils.parse(s)!!.time)
  }
}