package org.goblinframework.embedded.core.servlet

import org.junit.Assert.assertNotNull
import org.junit.Test

class HttpServletResponseAdapterTest {

  @Test
  fun getAdapter() {
    assertNotNull(HttpServletResponseAdapter.adapter)
  }
}