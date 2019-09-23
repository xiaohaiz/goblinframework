package org.goblinframework.core.event.dsl

import org.goblinframework.api.event.EventBus
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

class GoblinTimerEventGeneratorImpl internal constructor() {

  private val sequences = EnumMap<TimeUnit, AtomicLong>(TimeUnit::class.java)
  private val secondTimer: Timer
  private val minuteTimer: Timer

  init {
    sequences[TimeUnit.SECONDS] = AtomicLong()
    sequences[TimeUnit.MINUTES] = AtomicLong()

    val secondTimerTask = object : TimerTask() {
      override fun run() {
        publishEvent(TimeUnit.SECONDS)
      }
    }
    secondTimer = Timer("GoblinTimerEventGenerator-SECOND", true)
    secondTimer.scheduleAtFixedRate(secondTimerTask, 0, 1000)

    val minuteTimerTask = object : TimerTask() {
      override fun run() {
        publishEvent(TimeUnit.MINUTES)
      }
    }
    minuteTimer = Timer("GoblinTimerEventGenerator-MINUTES", true)
    minuteTimer.scheduleAtFixedRate(minuteTimerTask, 0, 60000)
  }

  internal fun close() {
    secondTimer.cancel()
    minuteTimer.cancel()
  }

  private fun publishEvent(unit: TimeUnit) {
    val sequence = sequences[unit]!!.getAndIncrement()
    val event = GoblinTimerEvent(unit, sequence)
    EventBus.publish("/goblin/timer", event)
  }
}
