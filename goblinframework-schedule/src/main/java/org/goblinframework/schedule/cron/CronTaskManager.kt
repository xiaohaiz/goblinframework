package org.goblinframework.schedule.cron

import org.goblinframework.api.common.Install
import org.goblinframework.api.schedule.CronTask
import org.goblinframework.api.schedule.ICronTaskManager
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.core.util.RandomUtils
import org.quartz.CronExpression
import org.quartz.CronTrigger
import org.quartz.JobDetail
import org.springframework.scheduling.quartz.CronTriggerFactoryBean
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import java.util.concurrent.ConcurrentHashMap

@GoblinManagedBean(type = "schedule")
class CronTaskManager private constructor()
  : GoblinManagedObject(), ICronTaskManager, CronTaskManagerMXBean {

  companion object {
    @JvmField val INSTANCE = CronTaskManager()
  }

  private val scheduler: SchedulerFactoryBean
  private val tasks = ConcurrentHashMap<String, Pair<JobDetail, CronTrigger>>()

  init {
    scheduler = SchedulerFactoryBean()
    scheduler.setSchedulerName(RandomUtils.nextObjectId())
    scheduler.setWaitForJobsToCompleteOnShutdown(true)
    scheduler.afterPropertiesSet()
    scheduler.start()
  }

  @Synchronized
  override fun register(task: CronTask) {
    tasks[task.name()]?.run { return }

    val cronExpression = CronExpression(task.cronExpression())

    val jobDetailFactoryBean = MethodInvokingJobDetailFactoryBean()
    jobDetailFactoryBean.setName("JobDetail-${task.name()}")
    jobDetailFactoryBean.targetObject = task
    jobDetailFactoryBean.targetMethod = "execute"
    jobDetailFactoryBean.setConcurrent(true)
    jobDetailFactoryBean.afterPropertiesSet()
    val jobDetail = jobDetailFactoryBean.getObject()!!

    val triggerFactoryBean = CronTriggerFactoryBean()
    triggerFactoryBean.setName("CronTrigger-${task.name()}")
    triggerFactoryBean.setJobDetail(jobDetail)
    triggerFactoryBean.setCronExpression(cronExpression.cronExpression)
    triggerFactoryBean.afterPropertiesSet()
    val cronTrigger = triggerFactoryBean.getObject()

    tasks[task.name()] = Pair(jobDetail, cronTrigger)

    scheduler.getObject()?.let {
      it.addJob(jobDetail, true)
      it.scheduleJob(cronTrigger)
    }
  }

  override fun unregister(name: String) {
    tasks.remove(name)?.let {
      scheduler.getObject()?.unscheduleJob(it.second.key)
      scheduler.getObject()?.deleteJob(it.first.key)
    }
  }

  override fun disposeBean() {
    scheduler.stop()
    scheduler.destroy()
  }

  @Install
  class Installer : ICronTaskManager by INSTANCE
}