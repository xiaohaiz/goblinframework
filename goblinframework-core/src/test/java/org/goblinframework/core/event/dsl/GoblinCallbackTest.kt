package org.goblinframework.core.event.dsl

import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.RandomUtils
import org.goblinframework.core.container.SpringManagedBean
import org.goblinframework.core.event.EventBus
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class GoblinCallbackTest : SpringManagedBean() {

  @Test
  fun testCallback() {
    val s = RandomStringUtils.randomAlphanumeric(64)
    Assert.assertEquals(s, echo(s))
    val i = RandomUtils.nextInt()
    Assert.assertEquals(i, echo(i))

    Assert.assertNull(echo(null))
  }

  private fun echo(input: Any?): Any? {
    return EventBus.execute { input }.get()
  }
}
