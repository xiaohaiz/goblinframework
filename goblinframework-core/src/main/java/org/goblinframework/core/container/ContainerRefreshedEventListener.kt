package org.goblinframework.core.container

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.event.GoblinEventChannel
import org.goblinframework.api.event.GoblinEventContext
import org.goblinframework.api.event.GoblinEventListener

@Singleton
@GoblinEventChannel("/goblin/core")
class ContainerRefreshedEventListener private constructor() : GoblinEventListener {

  companion object {
    @JvmField val INSTANCE = ContainerRefreshedEventListener()
  }

  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is ContainerRefreshedEvent
  }

  override fun onEvent(context: GoblinEventContext) {
  }
}