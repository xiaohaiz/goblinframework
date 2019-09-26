package org.goblinframework.core.system

import org.goblinframework.api.core.Singleton
import org.goblinframework.api.event.GoblinEventChannel
import org.goblinframework.api.event.GoblinEventContext
import org.goblinframework.api.event.GoblinEventListener
import org.goblinframework.api.system.ISubModule
import org.goblinframework.api.system.ModuleFinalizeContext
import org.goblinframework.api.system.ModuleInitializeContext
import org.goblinframework.api.system.ModuleInstallContext
import org.slf4j.LoggerFactory

@Singleton
@GoblinEventChannel("/goblin/core")
class SubModuleEventListener private constructor() : GoblinEventListener {

  companion object {
    private val logger = LoggerFactory.getLogger(SubModuleEventListener::class.java)

    @JvmField val INSTANCE = SubModuleEventListener()
  }

  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is SubModuleEvent
  }

  override fun onEvent(context: GoblinEventContext) {
    val event = context.event as SubModuleEvent
    if (event.ctx is ModuleInstallContext) {
      event.subModules.forEach { e ->
        logger.info("Install [{}]", e.id().fullName())
        event.ctx.setExtension<ISubModule>(e.javaClass.name, e)
        e.install(event.ctx as ModuleInstallContext)
      }
    } else if (event.ctx is ModuleInitializeContext) {
      event.subModules.forEach { e ->
        logger.info("Initialize [{}]", e.id().fullName())
        e.initialize(event.ctx as ModuleInitializeContext)
      }
    } else if (event.ctx is ModuleFinalizeContext) {
      event.subModules.forEach { e ->
        logger.info("Finalize [{}]", e.id().fullName())
        e.finalize(event.ctx as ModuleFinalizeContext)
      }
    } else {
      throw UnsupportedOperationException()
    }
  }


}
