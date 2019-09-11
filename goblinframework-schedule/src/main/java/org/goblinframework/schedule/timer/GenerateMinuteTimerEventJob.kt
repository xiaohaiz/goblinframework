package org.goblinframework.schedule.timer

import org.goblinframework.core.event.EventBus
import org.goblinframework.core.event.dsl.GoblinTimerEvent
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

class GenerateMinuteTimerEventJob private constructor() {

  companion object {
    @JvmField val INSTANCE = GenerateMinuteTimerEventJob()
  }

  private val sequence = AtomicLong()

  fun execute() {
    val event = GoblinTimerEvent(TimeUnit.MINUTES, sequence.getAndIncrement())
    EventBus.publish(event)
  }
}