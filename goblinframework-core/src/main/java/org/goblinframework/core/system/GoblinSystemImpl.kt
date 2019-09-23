package org.goblinframework.core.system

import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.core.bootstrap.GoblinModuleManager
import org.goblinframework.core.config.ConfigLoader
import org.goblinframework.core.container.SpringContainerManager
import org.goblinframework.core.event.boss.EventBusBoss
import org.goblinframework.core.util.ClassUtils

@GoblinManagedBean(type = "core", name = "GoblinSystem")
class GoblinSystemImpl internal constructor() : GoblinManagedObject(), GoblinSystemMXBean {

  internal fun applicationName(): String {
    return ConfigLoader.INSTANCE.getApplicationName()
  }

  override fun initializeBean() {
    ConfigLoader.INSTANCE.initialize()
    EventBusBoss.INSTANCE.initialize()
    ConfigLoader.INSTANCE.start()
    GoblinModuleManager.INSTANCE.executeInstall().executeInitialize()
  }

  override fun disposeBean() {
    SpringContainerManager.INSTANCE.dispose()
    GoblinModuleManager.INSTANCE.executeFinalize()
    ConfigLoader.INSTANCE.stop()
    EventBusBoss.INSTANCE.dispose()
    ConfigLoader.INSTANCE.dispose()
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