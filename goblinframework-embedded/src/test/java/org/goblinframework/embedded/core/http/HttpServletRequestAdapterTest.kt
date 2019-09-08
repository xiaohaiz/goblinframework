package org.goblinframework.embedded.core.http

import org.junit.Assert.assertNotNull
import org.junit.Test

class HttpServletRequestAdapterTest {

  @Test
  fun getAdapter() {
    assertNotNull(HttpServletRequestAdapter.adapter)
  }
}