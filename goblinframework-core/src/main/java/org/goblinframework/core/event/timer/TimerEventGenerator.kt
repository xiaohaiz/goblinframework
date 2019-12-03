package org.goblinframework.core.event.timer

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.function.Disposable
import org.goblinframework.core.schedule.ICronTaskManager
import java.util.*

@Singleton
class TimerEventGenerator private constructor() : Disposable {

  companion object {
    @JvmField val INSTANCE = TimerEventGenerator()
  }

  private val timers = Collections.synchronizedList(ArrayList<Timer>())

  fun install() {
    ICronTaskManager.instance()?.run {
      register(SecondTimerEventGenerator.INSTANCE)
      register(MinuteTimerEventGenerator.INSTANCE)
      return
    }
    val secondTimer = Timer("SecondTimerEventGenerator", true)
    secondTimer.scheduleAtFixedRate(object : TimerTask() {
      override fun run() {
        SecondTimerEventGenerator.INSTANCE.execute()
      }
    }, 0, 1000)
    timers.add(secondTimer)
    val minuteTimer = Timer("MinuteTimerEventGenerator", true)
    secondTimer.scheduleAtFixedRate(object : TimerTask() {
      override fun run() {
        MinuteTimerEventGenerator.INSTANCE.execute()
      }
    }, 0, 60000)
    timers.add(minuteTimer)
  }

  override fun dispose() {
    timers.forEach { it.cancel() }
    timers.clear()
  }
}
