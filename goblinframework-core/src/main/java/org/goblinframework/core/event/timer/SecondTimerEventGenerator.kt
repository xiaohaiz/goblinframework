package org.goblinframework.core.event.timer

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.event.EventBus
import org.goblinframework.api.schedule.CronConstants
import org.goblinframework.api.schedule.CronTask
import org.goblinframework.core.event.dsl.GoblinTimerEvent
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

@Singleton
class SecondTimerEventGenerator private constructor() : CronTask {

  companion object {
    @JvmField val INSTANCE = SecondTimerEventGenerator()
  }

  private val sequence = AtomicLong()

  override fun name(): String {
    return "SecondTimerEventGenerator"
  }

  override fun cronExpression(): String {
    return CronConstants.SECOND_TIMER
  }

  override fun execute() {
    val next = sequence.getAndIncrement()
    val event = GoblinTimerEvent(TimeUnit.SECONDS, next)
    EventBus.publish("/goblin/timer", event)
  }
}