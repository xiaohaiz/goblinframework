package org.goblinframework.database.mongo.reactor

import org.goblinframework.api.function.Disposable
import reactor.core.scheduler.Scheduler
import reactor.scheduler.forkjoin.ForkJoinPoolScheduler

class MongoSchedulerManager private constructor() : Disposable {

  companion object {
    @JvmField val INSTANCE = MongoSchedulerManager()
  }

  val scheduler: Scheduler

  init {
    scheduler = ForkJoinPoolScheduler.create("MongoScheduler(ForkJoin)")
  }

  override fun dispose() {
    scheduler.dispose()
  }
}