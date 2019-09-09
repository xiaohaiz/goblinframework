package org.goblinframework.webmvc.module.converter

import org.goblinframework.core.conversion.ConversionService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.springframework.http.HttpMethod
import org.springframework.web.bind.annotation.RequestMethod

class HttpMethodToRequestMethodConverterTest {

  @Test
  fun convert() {
    val cs = ConversionService.INSTANCE
    assertTrue(cs.canConvert(HttpMethod::class.java, RequestMethod::class.java))
    for (source in HttpMethod.values()) {
      val target = cs.convert(source, RequestMethod::class.java)
      assertEquals(source.name, target!!.name)
    }
  }
}