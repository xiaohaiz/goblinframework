package org.goblinframework.core.system

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.event.GoblinEventChannel
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener
import org.slf4j.LoggerFactory

@Singleton
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

  @Install
  @GoblinEventChannel("/goblin/core")
  class Installer : GoblinEventListener by INSTANCE

}
