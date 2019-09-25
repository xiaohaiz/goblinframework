package org.goblinframework.schedule.cron

import org.goblinframework.api.common.Install
import org.goblinframework.api.common.Singleton
import org.goblinframework.api.common.ThreadSafe
import org.goblinframework.api.schedule.CronTask
import org.goblinframework.api.schedule.ICronTaskManager
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Singleton
@ThreadSafe
@GoblinManagedBean(type = "Schedule")
class CronTaskManager private constructor() : GoblinManagedObject(), ICronTaskManager, CronTaskManagerMXBean {

  companion object {
    @JvmField val INSTANCE = CronTaskManager()
  }

  private val scheduler: SchedulerFactoryBean
  private val lock = ReentrantLock()
  private val tasks = mutableMapOf<String, ManagedCronTask>()

  init {
    scheduler = SchedulerFactoryBean()
    scheduler.setSchedulerName("CronTaskManagerScheduler")
    scheduler.setWaitForJobsToCompleteOnShutdown(true)
    scheduler.afterPropertiesSet()
    scheduler.start()
  }

  override fun register(task: CronTask) {
    lock.withLock {
      tasks[task.name()]?.run { return }
      val mct = ManagedCronTask(scheduler.getObject()!!, task)
      mct.initialize()
      tasks[task.name()] = mct
    }
  }

  override fun unregister(name: String) {
    lock.withLock { tasks.remove(name) }?.dispose()
  }

  override fun disposeBean() {
    lock.withLock {
      tasks.values.forEach { it.dispose() }
      tasks.clear()
    }
    scheduler.stop()
    scheduler.destroy()
  }

  @Install
  class Installer : ICronTaskManager by INSTANCE
}