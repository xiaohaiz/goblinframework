package org.goblinframework.schedule.cron

import org.goblinframework.api.monitor.Flight
import org.goblinframework.api.monitor.FlightLocation
import org.goblinframework.api.schedule.CronTask
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.core.monitor.FlightRecorder
import org.quartz.CronExpression
import org.quartz.CronTrigger
import org.quartz.JobDetail
import org.quartz.Scheduler
import org.springframework.scheduling.quartz.CronTriggerFactoryBean
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean

@GoblinManagedBean(type = "Schedule", name = "CronTask")
class ManagedCronTask
internal constructor(private val scheduler: Scheduler, private val task: CronTask)
  : GoblinManagedObject(), CronTaskMXBean {

  private val jobDetail: JobDetail
  private val cronTrigger: CronTrigger

  init {
    val cronExpression = CronExpression(task.cronExpression())

    val jobDetailFactoryBean = MethodInvokingJobDetailFactoryBean()
    jobDetailFactoryBean.setName("JobDetail-${task.name()}")
    jobDetailFactoryBean.targetObject = this
    jobDetailFactoryBean.targetMethod = "execute"
    jobDetailFactoryBean.setConcurrent(true)
    jobDetailFactoryBean.afterPropertiesSet()
    jobDetail = jobDetailFactoryBean.getObject()!!

    val triggerFactoryBean = CronTriggerFactoryBean()
    triggerFactoryBean.setName("CronTrigger-${task.name()}")
    triggerFactoryBean.setJobDetail(jobDetail)
    triggerFactoryBean.setCronExpression(cronExpression.cronExpression)
    triggerFactoryBean.afterPropertiesSet()
    cronTrigger = triggerFactoryBean.getObject()!!
  }

  /**
   * This method is required for invocation by quartz job detail.
   * See job detail creation in constructor.
   */
  override fun execute() {
    FlightLocation.builder()
        .startPoint(Flight.StartPoint.CTK)
        .clazz(task.javaClass)
        .method("execute")
        .attribute("flight.silence", true)
        .build()
        .launch()
    try {
      task.execute()
    } finally {
      FlightRecorder.getFlightMonitor()?.terminateFlight()
    }
  }

  override fun initializeBean() {
    scheduler.addJob(jobDetail, true)
    scheduler.scheduleJob(cronTrigger)
  }

  override fun disposeBean() {
    scheduler.unscheduleJob(cronTrigger.key)
    scheduler.deleteJob(jobDetail.key)
  }

  override fun getName(): String {
    return task.name()
  }

  override fun getCronExpression(): String {
    return task.cronExpression()
  }
}