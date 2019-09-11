package org.goblinframework.schedule.timer

import org.goblinframework.core.event.EventBus
import org.goblinframework.core.event.dsl.GoblinTimerEvent
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

class GenerateSecondTimerEventJob private constructor() {

  companion object {
    @JvmField val INSTANCE = GenerateSecondTimerEventJob()
  }

  private val sequence = AtomicLong()

  fun execute() {
    val event = GoblinTimerEvent(TimeUnit.SECONDS, sequence.getAndIncrement())
    EventBus.publish(event)
  }
}