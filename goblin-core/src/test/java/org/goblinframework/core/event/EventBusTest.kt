package org.goblinframework.core.event

import org.goblinframework.core.event.exception.EventBossChannelNotFoundException
import org.goblinframework.core.event.exception.EventBossListenerNotFoundException
import org.goblinframework.core.event.exception.EventBusException
import org.goblinframework.core.util.RandomUtils
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class EventBusTest {

  class EventBusTestEvent : GoblinEvent()

  @Test
  fun channelNotFound() {
    val channel = "/${RandomUtils.nextObjectId()}"
    val future = EventBus.publish(channel, EventBusTestEvent())
    try {
      future.get()
      fail()
    } catch (ex: EventBusException) {
      assertTrue(ex.cause is EventBossChannelNotFoundException)
    }
  }

  @Test
  fun listenerNotFound() {
    val channel = "/${RandomUtils.nextObjectId()}"
    EventBus.register(channel, 32, 1)
    try {
      val future = EventBus.publish(channel, EventBusTestEvent())
      try {
        future.get()
        fail()
      } catch (ex: EventBusException) {
        assertTrue(ex.cause is EventBossListenerNotFoundException)
      }
    } finally {
      EventBus.unregister(channel)
    }
  }

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