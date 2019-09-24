package org.goblinframework.schedule.timer

import org.goblinframework.core.util.RandomUtils
import org.quartz.CronExpression
import org.quartz.CronTrigger
import org.springframework.scheduling.quartz.CronTriggerFactoryBean
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean
import org.springframework.scheduling.quartz.SchedulerFactoryBean

class GoblinTimerEventGeneratorImpl internal constructor() {

  private val scheduler: SchedulerFactoryBean

  init {
    val secondCronTrigger = createCronTrigger("*/1 * * * * ?", GenerateSecondTimerEventJob.INSTANCE)
    val minuteCronTrigger = createCronTrigger("0 0/1 * * * ?", GenerateMinuteTimerEventJob.INSTANCE)

    scheduler = SchedulerFactoryBean()
    scheduler.setSchedulerName(RandomUtils.nextObjectId())
    scheduler.setTriggers(secondCronTrigger, minuteCronTrigger)
    scheduler.setWaitForJobsToCompleteOnShutdown(true)
    scheduler.afterPropertiesSet()
    scheduler.start()
  }

  internal fun close() {
    scheduler.stop()
    scheduler.destroy()
  }

  private fun createCronTrigger(cron: String, job: Any): CronTrigger {
    val cronExpression = CronExpression(cron)

    val jobDetailFactoryBean = MethodInvokingJobDetailFactoryBean()
    jobDetailFactoryBean.setName(RandomUtils.nextObjectId())
    jobDetailFactoryBean.targetObject = job
    jobDetailFactoryBean.targetMethod = "execute"
    jobDetailFactoryBean.setConcurrent(true)
    jobDetailFactoryBean.afterPropertiesSet()
    val jobDetail = jobDetailFactoryBean.`object`!!

    val triggerFactoryBean = CronTriggerFactoryBean()
    triggerFactoryBean.setName(RandomUtils.nextObjectId())
    triggerFactoryBean.setJobDetail(jobDetail)
    triggerFactoryBean.setCronExpression(cronExpression.cronExpression)
    triggerFactoryBean.afterPropertiesSet()
    return triggerFactoryBean.getObject()!!
  }
}
