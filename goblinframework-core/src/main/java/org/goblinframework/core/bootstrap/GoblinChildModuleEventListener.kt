package org.goblinframework.core.bootstrap

import org.goblinframework.core.event.GoblinEventChannel
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener
import org.slf4j.LoggerFactory

@GoblinEventChannel("/goblin/core")
class GoblinChildModuleEventListener private constructor() : GoblinEventListener {

  companion object {
    private val logger = LoggerFactory.getLogger(GoblinChildModuleEventListener::class.java)
    @JvmField val INSTANCE = GoblinChildModuleEventListener()
  }

  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is GoblinChildModuleEvent
  }

  override fun onEvent(context: GoblinEventContext) {
    val event = context.event as GoblinChildModuleEvent
    when (event.ctx) {
      is GoblinModuleInstallContext -> {
        event.childModules.forEach {
          it.install(event.ctx)
          logger.info("Install [${it.name()}]")
        }
      }
      is GoblinModuleInitializeContext -> {
        event.childModules.forEach {
          it.initialize(event.ctx)
          logger.info("Initialize [${it.name()}]")
        }
      }
      is GoblinModuleFinalizeContext -> {
        event.childModules.reversed().forEach {
          it.finalize(event.ctx)
          logger.info("Finalize [${it.name()}]")
        }
      }
    }
  }
}