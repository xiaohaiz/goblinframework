package org.goblinframework.core.system

import org.goblinframework.core.config.ConfigManager
import org.goblinframework.core.container.SpringContainerManager
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import reactor.core.scheduler.Schedulers
import java.util.concurrent.CountDownLatch
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
    val timeoutLatch = CountDownLatch(1)
    val scheduler = Schedulers.newSingle("FinalizeModule", true)
    val finalizeTask = scheduler.schedule {
      try {
        for (module in ExtModuleLoader.asList().reversed()) {
          logger.info("Finalize {${module.id()}}")
          module.finalize(ExtModuleFinalizeContextImpl.INSTANCE)
        }
        for (id in GoblinModule.values().reversed()) {
          val module = ModuleLoader.module(id) ?: continue
          logger.info("Finalize {${module.id()}}")
          module.finalize(ModuleFinalizeContextImpl.INSTANCE)
        }
      } finally {
        timeoutLatch.countDown()
      }
    }
    if (!timeoutLatch.await(1, TimeUnit.MINUTES)) {
      finalizeTask.dispose()
    }
    scheduler.dispose()
  }

}