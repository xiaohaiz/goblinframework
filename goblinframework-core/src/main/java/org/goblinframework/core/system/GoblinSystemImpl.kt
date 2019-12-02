package org.goblinframework.core.system

import org.goblinframework.core.config.ConfigManager
import org.goblinframework.core.container.SpringContainerManager
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
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
      ExtModuleInstallContextImpl.INSTANCE.setExtension(module.javaClass.name, module)
      module.install(ExtModuleInstallContextImpl.INSTANCE)
    }

    // Execute INITIALIZE
    for (id in GoblinModule.values()) {
      val module = ModuleLoader.module(id) ?: continue
      logger.info("Initialize {${module.id()}}")
      module.initialize(ModuleInitializeContextImpl.INSTANCE)
    }
    for (module in ExtModuleLoader.asList()) {
      logger.info("Initialize {${module.id()}}")
      module.initialize(ExtModuleInitializeContextImpl.INSTANCE)
    }
  }

  override fun disposeBean() {
    logger.info("Shutdown GOBLIN system")

    // Execute prior finalization tasks
    PriorFinalizationTaskManager.INSTANCE.dispose()

    // Close spring container
    SpringContainerManager.INSTANCE.dispose()

    // Execute FINALIZE
    val executorService = Executors.newFixedThreadPool(1)
    executorService.submit {
      for (module in ExtModuleLoader.asList().reversed()) {
        logger.info("Finalize {${module.id()}}")
        module.finalize(ExtModuleFinalizeContextImpl.INSTANCE)
      }
      for (id in GoblinModule.values().reversed()) {
        val module = ModuleLoader.module(id) ?: continue
        logger.info("Finalize {${module.id()}}")
        module.finalize(ModuleFinalizeContextImpl.INSTANCE)
      }
    }
    executorService.shutdown()
    executorService.awaitTermination(1, TimeUnit.MINUTES)
  }

}