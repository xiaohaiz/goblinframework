package org.goblinframework.core.conversion

import org.junit.Assert.*
import org.junit.Test
import java.util.*

class ConversionServiceTest {

  @Test
  fun getNativeConversionService() {
    val cs = ConversionService.INSTANCE.getNativeConversionService()
    assertNotNull(cs)
  }

  @Test
  fun dateToLong() {
    val cs = ConversionService.INSTANCE
    assertTrue(cs.canConvert(Date::class.java, Long::class.java))
    val source = Date()
    val target = cs.convert(source, Long::class.java)
    assertEquals(source.time, target!!.toLong())
  }
}