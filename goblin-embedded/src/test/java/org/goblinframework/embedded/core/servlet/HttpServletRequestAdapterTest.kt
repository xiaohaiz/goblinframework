package org.goblinframework.embedded.core.servlet

import org.junit.Assert.assertNotNull
import org.junit.Test

class HttpServletRequestAdapterTest {

  @Test
  fun getAdapter() {
    assertNotNull(HttpServletRequestAdapter.adapter)
  }
}