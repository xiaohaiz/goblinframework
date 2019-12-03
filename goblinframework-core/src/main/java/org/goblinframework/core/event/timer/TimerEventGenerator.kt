package org.goblinframework.core.event.timer

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.schedule.ICronTaskManager
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import java.util.*

@Singleton
@GoblinManagedBean("Core")
class TimerEventGenerator private constructor() :
    GoblinManagedObject(), TimerEventGeneratorMXBean {

  companion object {
    @JvmField val INSTANCE = TimerEventGenerator()
  }

  private val timers = Collections.synchronizedList(ArrayList<Timer>())

  override fun initializeBean() {
    ICronTaskManager.instance()?.run {
      register(SecondTimerEventGenerator.INSTANCE)
      register(MinuteTimerEventGenerator.INSTANCE)
    } ?: kotlin.run {
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
  }

  override fun disposeBean() {
    ICronTaskManager.instance()?.run {
      unregister(SecondTimerEventGenerator.INSTANCE.name())
      unregister(MinuteTimerEventGenerator.INSTANCE.name())
    } ?: kotlin.run {
      timers.forEach { it.cancel() }
      timers.clear()
    }
  }
}
