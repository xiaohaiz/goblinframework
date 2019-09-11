package org.goblinframework.core.util

import org.junit.Assert.assertNotNull
import org.junit.Test

class RandomUtilsTest {

  @Test
  fun getRandom() {
    assertNotNull(RandomUtils.getRandom())
  }
}