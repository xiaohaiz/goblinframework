package org.goblinframework.core.system

import org.goblinframework.api.core.GoblinManagedBean
import org.goblinframework.api.core.GoblinManagedObject
import org.goblinframework.api.system.GoblinModule
import org.goblinframework.api.system.RuntimeMode
import org.goblinframework.core.config.ConfigManager
import org.goblinframework.core.container.SpringContainerManager
import org.goblinframework.core.module.SystemModule
import org.goblinframework.core.util.ClassUtils
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@GoblinManagedBean(type = "core", name = "GoblinSystem")
class GoblinSystemImpl
internal constructor(private val systemManager: GoblinSystemManager)
  : GoblinManagedObject(), GoblinSystemMXBean {

  internal fun applicationName(): String {
    return ConfigManager.INSTANCE.getApplicationName()
  }

  internal fun runtimeMode(): RuntimeMode {
    return ConfigManager.INSTANCE.getRuntimeMode()
  }

  override fun initializeBean() {
    // Execute INSTALL
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

    // Execute INITIALIZE
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

    systemManager.priorFinalizationTasks.forEach {
      try {
        it.apply()
      } catch (ex: Exception) {
        val logger = LoggerFactory.getLogger(SystemModule::class.java)
        logger.error("Exception raised when executing PriorFinalizationTask: $it")
      }
    }

    // Close spring container
    SpringContainerManager.INSTANCE.dispose()

    // Execute FINALIZE
    val executorService = Executors.newFixedThreadPool(1)
    executorService.submit {
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
    executorService.shutdown()
    executorService.awaitTermination(1, TimeUnit.MINUTES)

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