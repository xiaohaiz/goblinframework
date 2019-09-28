package org.goblinframework.remote.server.service

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.container.ContainerRefreshedEvent
import org.goblinframework.core.event.GoblinEventChannel
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener

@Singleton
@GoblinEventChannel("/goblin/core")
class ExposeSpringContainer private constructor() : GoblinEventListener {

  companion object {
    @JvmField val INSTANCE = ExposeSpringContainer()
  }

  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is ContainerRefreshedEvent
  }

  override fun onEvent(context: GoblinEventContext) {
    val event = context.event as ContainerRefreshedEvent
    ExposeServiceScanner.scan(event.applicationContext)
  }
}