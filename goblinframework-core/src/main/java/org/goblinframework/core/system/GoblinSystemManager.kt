package org.goblinframework.core.system

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.core.Lifecycle
import org.goblinframework.api.function.Block0
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.util.RandomUtils
import java.util.concurrent.atomic.AtomicReference

@Singleton
@GoblinManagedBean(type = "Core")
class GoblinSystemManager private constructor() :
    GoblinManagedObject(), Lifecycle, GoblinSystemManagerMXBean {

  companion object {
    @JvmField val INSTANCE = GoblinSystemManager()
  }

  private val applicationId = RandomUtils.nextObjectId()
  private val running = AtomicReference<GoblinSystemImpl>()
  internal val priorFinalizationTasks = mutableListOf<Block0>()

  @Synchronized
  override fun start() {
    if (running.get() == null) {
      val gs = GoblinSystemImpl(this)
      gs.initialize()
      running.set(gs)
    }
  }

  override fun stop() {
    running.getAndSet(null)?.dispose()
    dispose()
  }

  override fun isRunning(): Boolean {
    return running.get() != null
  }

  fun applicationId(): String {
    return applicationId
  }

  fun applicationName(): String {
    return running.get()?.applicationName() ?: throw GoblinSystemException("GOBLIN system not started")
  }

  fun runtimeMode(): RuntimeMode {
    return running.get()?.runtimeMode() ?: throw GoblinSystemException("GOBLIN system not started")
  }

  fun registerPriorFinalizationTask(action: Block0) {
    priorFinalizationTasks.add(action)
  }
}