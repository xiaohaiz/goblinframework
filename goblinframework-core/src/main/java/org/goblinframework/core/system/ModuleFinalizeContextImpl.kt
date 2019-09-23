package org.goblinframework.core.system

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.event.EventBus
import org.goblinframework.api.event.GoblinEventListener
import org.goblinframework.api.system.ModuleFinalizeContext

@Singleton
class ModuleFinalizeContextImpl private constructor() : ModuleContextImpl(), ModuleFinalizeContext {

  companion object {
    @JvmField val INSTANCE = ModuleFinalizeContextImpl()
  }

  override fun unsubscribeEventLister(listener: GoblinEventListener) {
    EventBus.unsubscribe(listener)
  }
}