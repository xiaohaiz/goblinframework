package org.goblinframework.core.reactor

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.util.NamedDaemonThreadFactory
import org.goblinframework.core.util.SystemUtils
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

@Singleton
class CoreScheduler private constructor() : GoblinManagedObject(), CoreSchedulerMXBean {

  companion object {
    @JvmField val INSTANCE = CoreScheduler()
  }

  private val scheduler: Scheduler

  init {
    val threadCap = SystemUtils.estimateThreads() * 10
    val queuedTaskCap = 102400
    val threadFactory = NamedDaemonThreadFactory.getInstance("CoreScheduler")
    val ttlSeconds = 60
    scheduler = Schedulers.newBoundedElastic(threadCap, queuedTaskCap, threadFactory, ttlSeconds)
  }

  fun get(): Scheduler {
    return scheduler
  }

  override fun disposeBean() {
    scheduler.dispose()
    logger.debug("Core scheduler disposed")
  }
}