package org.goblinframework.embedded.core.http

import org.junit.Assert
import org.junit.Test

class HttpServletResponseAdapterTest {

  @Test
  fun getAdapter() {
    Assert.assertNotNull(HttpServletResponseAdapter.adapter)
  }
}