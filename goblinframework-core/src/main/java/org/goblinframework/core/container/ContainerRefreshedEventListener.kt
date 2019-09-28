package org.goblinframework.core.container

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.event.GoblinEventChannel
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener

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