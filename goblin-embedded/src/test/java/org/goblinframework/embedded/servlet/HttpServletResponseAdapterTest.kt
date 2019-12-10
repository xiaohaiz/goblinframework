package org.goblinframework.embedded.servlet

import org.junit.Assert.assertNotNull
import org.junit.Test

class HttpServletResponseAdapterTest {

  @Test
  fun getAdapter() {
    assertNotNull(HttpServletResponseAdapter.adapter)
  }
}