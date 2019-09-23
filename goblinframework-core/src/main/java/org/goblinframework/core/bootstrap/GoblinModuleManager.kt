package org.goblinframework.core.bootstrap

import org.goblinframework.core.event.EventBus
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class GoblinModuleManager private constructor() {

  companion object {
    private val logger = LoggerFactory.getLogger(GoblinModuleManager::class.java)
    @JvmField val INSTANCE = GoblinModuleManager()
  }

  init {
    EventBus.subscribe(GoblinChildModuleEventListener.INSTANCE)
  }

  private val install = AtomicBoolean()
  private val initialize = AtomicBoolean()
  private val finalize = AtomicBoolean()

  fun executeInstall(): GoblinModuleManager {
    if (!install.compareAndSet(false, true)) {
      return this
    }
    val ctx = GoblinModuleInstallContext.INSTANCE
    for (name in GoblinModuleDefinition.moduleNames) {
      val module = GoblinModuleLoader.INSTANCE.getGoblinModule(name) ?: continue
      module.install(ctx)
      logger.info("Install {${module.name()}}")
    }
    for (module in GoblinExtensionModuleLoader.INSTANCE.getGoblinExtensionModules()) {
      module.install(ctx)
      logger.info("Install (${module.name()})")
    }
    return this
  }

  fun executeInitialize() {
    if (!initialize.compareAndSet(false, true)) {
      return
    }
    val ctx = GoblinModuleInitializeContext()
    for (name in GoblinModuleDefinition.moduleNames) {
      val module = GoblinModuleLoader.INSTANCE.getGoblinModule(name) ?: continue
      module.initialize(ctx)
      logger.info("Initialize {${module.name()}}")
    }
    for (module in GoblinExtensionModuleLoader.INSTANCE.getGoblinExtensionModules()) {
      module.initialize(ctx)
      logger.info("Initialize (${module.name()})")
    }
  }

  fun executeFinalize() {
    if (!finalize.compareAndSet(false, true)) {
      return
    }
    val future = EventBus.execute {
      val ctx = GoblinModuleFinalizeContext()
      for (name in GoblinModuleDefinition.moduleNames.reversed()) {
        val module = GoblinModuleLoader.INSTANCE.getGoblinModule(name) ?: continue
        module.finalize(ctx)
        logger.info("Finalize {${module.name()}}")
      }
      for (module in GoblinExtensionModuleLoader.INSTANCE.getGoblinExtensionModules().reversed()) {
        module.finalize(ctx)
        logger.info("Finalize (${module.name()})")
      }
    }
    try {
      future.awaitUninterruptibly(1, TimeUnit.MINUTES)
    } catch (ignore: Throwable) {
    }
  }

}