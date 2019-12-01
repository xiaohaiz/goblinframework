package org.goblinframework.core.system

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.function.Block0
import org.goblinframework.core.config.ConfigManager
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.util.ClassUtils
import org.goblinframework.core.util.RandomUtils
import java.util.concurrent.atomic.AtomicReference

@Singleton
@GoblinManagedBean(type = "Core")
class GoblinSystemManager private constructor() :
    GoblinManagedObject(), GoblinSystemManagerMXBean {

  companion object {
    @JvmField val INSTANCE = GoblinSystemManager()
  }

  private val applicationId = RandomUtils.nextObjectId()
  private val systemReference = AtomicReference<GoblinSystemImpl?>()
  internal val priorFinalizationTasks = mutableListOf<Block0>()

  override fun initializeBean() {
    ConfigManager.INSTANCE.initialize()
    val system = GoblinSystemImpl(this)
    system.initialize()
    systemReference.set(system)
  }

  fun applicationId(): String {
    return applicationId
  }

  fun applicationName(): String {
    return ConfigManager.INSTANCE.getApplicationName()
  }

  fun runtimeMode(): RuntimeMode {
    return ConfigManager.INSTANCE.getRuntimeMode()
  }

  fun registerPriorFinalizationTask(action: Block0) {
    priorFinalizationTasks.add(action)
  }

  override fun disposeBean() {
    systemReference.getAndSet(null)?.dispose()
    ConfigManager.INSTANCE.dispose()

    logger.info("FAREWELL")
    shutdownLog4j2IfNecessary()
  }

  private fun shutdownLog4j2IfNecessary() {
    try {
      val clazz = ClassUtils.loadClass("org.apache.logging.log4j.LogManager")
      val method = clazz.getMethod("shutdown")
      method.invoke(null)
    } catch (ignore: Exception) {
    }
  }
}