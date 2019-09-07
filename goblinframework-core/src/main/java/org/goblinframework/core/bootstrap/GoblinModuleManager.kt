package org.goblinframework.core.bootstrap

import org.goblinframework.core.event.GoblinEventBus
import java.util.concurrent.atomic.AtomicBoolean

class GoblinModuleManager private constructor() {

  companion object {
    @JvmField val INSTANCE = GoblinModuleManager()
  }

  init {
    GoblinEventBus.subscribe(GoblinChildModuleEventListener.INSTANCE)
  }

  private val initialize = AtomicBoolean()
  private val bootstrap = AtomicBoolean()
  private val shutdown = AtomicBoolean()
  private val finalize = AtomicBoolean()

  fun initialize(): GoblinModuleManager {
    if (!initialize.compareAndSet(false, true)) {
      return this
    }
    val ctx = GoblinModuleInitializeContext()
    for (name in GoblinModuleDefinition.moduleNames) {
      val module = GoblinModuleLoader.INSTANCE.getGoblinModule(name) ?: continue
      module.initialize(ctx)
    }
    return this
  }

  fun bootstrap() {
    if (!bootstrap.compareAndSet(false, true)) {
      return
    }
    val ctx = GoblinModuleBootstrapContext()
    for (name in GoblinModuleDefinition.moduleNames) {
      val module = GoblinModuleLoader.INSTANCE.getGoblinModule(name) ?: continue
      module.bootstrap(ctx)
    }
  }

  fun shutdown(): GoblinModuleManager {
    if (!shutdown.compareAndSet(false, true)) {
      return this
    }
    val ctx = GoblinModuleShutdownContext()
    for (name in GoblinModuleDefinition.moduleNames.reversed()) {
      val module = GoblinModuleLoader.INSTANCE.getGoblinModule(name) ?: continue
      module.shutdown(ctx)
    }
    return this
  }

  fun finalize() {
    if (!finalize.compareAndSet(false, true)) {
      return
    }
    val ctx = GoblinModuleFinalizeContext()
    for (name in GoblinModuleDefinition.moduleNames.reversed()) {
      val module = GoblinModuleLoader.INSTANCE.getGoblinModule(name) ?: continue
      module.finalize(ctx)
    }
  }

}