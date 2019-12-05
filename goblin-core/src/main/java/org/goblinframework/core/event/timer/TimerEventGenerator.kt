package org.goblinframework.core.event.timer

import org.goblinframework.core.schedule.ICronTaskManager
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import java.util.*

@GoblinManagedBean("Core")
class TimerEventGenerator : GoblinManagedObject(), TimerEventGeneratorMXBean {

  private val secondTimerEventGenerator = SecondTimerEventGenerator()
  private val minuteTimerEventGenerator = MinuteTimerEventGenerator()
  private val timers = Collections.synchronizedList(ArrayList<Timer>())

  override fun initializeBean() {
    ICronTaskManager.instance()?.run {
      register(secondTimerEventGenerator)
      register(minuteTimerEventGenerator)
    } ?: kotlin.run {
      val secondTimer = Timer("SecondTimerEventGenerator", true)
      secondTimer.scheduleAtFixedRate(object : TimerTask() {
        override fun run() {
          secondTimerEventGenerator.execute()
        }
      }, 0, 1000)
      timers.add(secondTimer)
      val minuteTimer = Timer("MinuteTimerEventGenerator", true)
      secondTimer.scheduleAtFixedRate(object : TimerTask() {
        override fun run() {
          minuteTimerEventGenerator.execute()
        }
      }, 0, 60000)
      timers.add(minuteTimer)
    }
    logger.debug("{EventBus} Timer event generator started")
  }

  override fun disposeBean() {
    ICronTaskManager.instance()?.run {
      unregister(secondTimerEventGenerator.name())
      unregister(minuteTimerEventGenerator.name())
    } ?: kotlin.run {
      timers.forEach { it.cancel() }
      timers.clear()
    }
    logger.debug("{EventBus} Timer event generator disposed")
  }
}
