package org.goblinframework.core.system

import org.goblinframework.api.event.EventBus
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.api.system.GoblinModule
import org.goblinframework.core.config.ConfigLoader
import org.goblinframework.core.config.ConfigParserManager
import org.goblinframework.core.container.SpringContainerManager
import org.goblinframework.core.event.boss.EventBusBoss
import org.goblinframework.core.util.ClassUtils
import java.util.concurrent.TimeUnit

@GoblinManagedBean(type = "core", name = "GoblinSystem")
class GoblinSystemImpl internal constructor() : GoblinManagedObject(), GoblinSystemMXBean {

  internal fun applicationName(): String {
    return ConfigLoader.INSTANCE.getApplicationName()
  }

  override fun initializeBean() {
    ConfigLoader.INSTANCE.initialize()
    EventBusBoss.INSTANCE.initialize()
    ConfigLoader.INSTANCE.start()

    EventBus.subscribe(SubModuleEventListener.INSTANCE)

    for (id in GoblinModule.values()) {
      val module = ModuleLoader.module(id) ?: continue
      logger.info("Install {${module.id()}}")
      ModuleInstallContextImpl.INSTANCE.setExtension(module.javaClass.name, module)
      module.install(ModuleInstallContextImpl.INSTANCE)
    }
    for (module in ExtModuleLoader.asList()) {
      logger.info("Install {${module.id()}}")
      ModuleInstallContextImpl.INSTANCE.setExtension(module.javaClass.name, module)
      module.install(ModuleInstallContextImpl.INSTANCE)
    }

    ConfigParserManager.INSTANCE.asList().forEach { it.initialize() }

    for (id in GoblinModule.values()) {
      val module = ModuleLoader.module(id) ?: continue
      logger.info("Initialize {${module.id()}}")
      module.initialize(ModuleInitializeContextImpl.INSTANCE)
    }
    for (module in ExtModuleLoader.asList()) {
      logger.info("Initialize {${module.id()}}")
      module.initialize(ModuleInitializeContextImpl.INSTANCE)
    }
  }

  override fun disposeBean() {
    logger.info("Shutdown GOBLIN system")
    SpringContainerManager.INSTANCE.dispose()

    val future = EventBus.execute {
      for (module in ExtModuleLoader.asList().reversed()) {
        logger.info("Finalize {${module.id()}}")
        module.finalize(ModuleFinalizeContextImpl.INSTANCE)
      }
      for (id in GoblinModule.values().reversed()) {
        val module = ModuleLoader.module(id) ?: continue
        logger.info("Finalize {${module.id()}}")
        module.finalize(ModuleFinalizeContextImpl.INSTANCE)
      }
    }
    try {
      future.awaitUninterruptibly(1, TimeUnit.MINUTES)
    } catch (ignore: Throwable) {
    }
    EventBus.unsubscribe(SubModuleEventListener.INSTANCE)

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