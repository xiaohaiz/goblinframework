package org.goblinframework.core.event

import org.goblinframework.core.util.RandomUtils
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class EventBusTest {
  @Test
  fun execute() {
    val s = RandomUtils.randomAlphanumeric(64)
    assertEquals(s, echo(s))
    val i = RandomUtils.nextInt()
    assertEquals(i, echo(i))
    assertNull(echo(null))
  }

  private fun echo(input: Any?): Any? {
    return EventBus.execute { input }.get()
  }
}