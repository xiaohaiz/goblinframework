package org.goblinframework.embedded.servlet

import org.junit.Assert.assertNotNull
import org.junit.Test

class HttpServletRequestAdapterTest {

  @Test
  fun getAdapter() {
    assertNotNull(HttpServletRequestAdapter.adapter)
  }
}