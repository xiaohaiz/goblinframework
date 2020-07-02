package org.goblinframework.core.util

import org.junit.Assert
import org.junit.Test

class NetworkUtilsTest {

  @Test
  fun getLocalAddress() {
    val addr = NetworkUtils.getLocalAddress()
    Assert.assertNotNull(addr)
  }
}