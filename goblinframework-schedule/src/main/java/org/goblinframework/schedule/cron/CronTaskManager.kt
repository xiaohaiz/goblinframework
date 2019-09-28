package org.goblinframework.schedule.cron

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.schedule.CronTask
import org.goblinframework.core.schedule.ICronTaskManager
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
@ThreadSafe
@GoblinManagedBean(type = "Schedule")
class CronTaskManager private constructor() : GoblinManagedObject(), ICronTaskManager, CronTaskManagerMXBean {

  companion object {
    @JvmField val INSTANCE = CronTaskManager()
  }

  private val scheduler: SchedulerFactoryBean
  private val lock = ReentrantReadWriteLock()
  private val tasks = mutableMapOf<String, ManagedCronTask>()

  init {
    scheduler = SchedulerFactoryBean()
    scheduler.setSchedulerName("CronTaskManagerScheduler")
    scheduler.setWaitForJobsToCompleteOnShutdown(true)
    scheduler.afterPropertiesSet()
    scheduler.start()
  }

  override fun register(task: CronTask) {
    lock.write {
      tasks[task.name()]?.run { return }
      val mct = ManagedCronTask(scheduler.getObject()!!, task)
      mct.initialize()
      tasks[task.name()] = mct
    }
  }

  override fun unregister(name: String) {
    lock.write { tasks.remove(name) }?.dispose()
  }

  override fun disposeBean() {
    lock.write {
      tasks.values.forEach { it.dispose() }
      tasks.clear()
    }
    scheduler.stop()
    scheduler.destroy()
  }

  override fun getCronTaskList(): Array<CronTaskMXBean> {
    return lock.read { tasks.values.toTypedArray() }
  }

  @Install
  class Installer : ICronTaskManager by INSTANCE
}