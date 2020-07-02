package org.goblinframework.core.system

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.api.function.Block0
import org.goblinframework.core.service.GoblinManagedObject

@Singleton
@ThreadSafe
class PriorFinalizationTaskManager private constructor()
  : GoblinManagedObject(), PriorFinalizationTaskManagerMXBean {

  companion object {
    @JvmField val INSTANCE = PriorFinalizationTaskManager()
  }

  private val tasks = mutableListOf<Block0>()

  @Synchronized
  fun register(task: Block0) {
    tasks.add(task)
  }

  @Synchronized
  override fun disposeBean() {
    tasks.forEach {
      try {
        it.apply()
      } catch (ex: Throwable) {
        logger.warn("Exception raised when executing PriorFinalizationTask: $it", ex)
      }
    }
  }
}