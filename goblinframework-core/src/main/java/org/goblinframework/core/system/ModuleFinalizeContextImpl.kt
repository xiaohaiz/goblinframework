package org.goblinframework.core.system

import org.goblinframework.api.core.Singleton
import org.goblinframework.api.event.EventBus
import org.goblinframework.api.event.GoblinEventListener

@Singleton
class ModuleFinalizeContextImpl private constructor() : ModuleContextImpl(), ModuleFinalizeContext {

  companion object {
    @JvmField val INSTANCE = ModuleFinalizeContextImpl()
  }

  override fun unsubscribeEventLister(listener: GoblinEventListener) {
    EventBus.unsubscribe(listener)
  }
}