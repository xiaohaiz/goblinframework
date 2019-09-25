package org.goblinframework.core.event.timer

import org.goblinframework.api.common.Block1
import org.goblinframework.api.common.Singleton
import org.goblinframework.api.event.EventBus
import org.goblinframework.api.monitor.FlightAttribute
import org.goblinframework.api.schedule.CronConstants
import org.goblinframework.api.schedule.CronTask
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

@Singleton
class MinuteTimerEventGenerator private constructor() : CronTask {

  companion object {
    @JvmField val INSTANCE = MinuteTimerEventGenerator()
  }

  private val sequence = AtomicLong()

  override fun name(): String {
    return "MinuteTimerEventGenerator"
  }

  override fun cronExpression(): String {
    return CronConstants.MINUTE_TIMER
  }

  override fun concurrent(): Boolean {
    return true
  }

  override fun flightAttribute(): Block1<FlightAttribute> {
    return Block1 { it.setAttribute("flight.silence", true) }
  }

  override fun execute() {
    val next = sequence.getAndIncrement()
    val event = GoblinTimerEvent(TimeUnit.MINUTES, next)
    EventBus.publish("/goblin/timer", event)
  }
}