package org.goblinframework.remote.server.service

import org.goblinframework.api.core.Singleton
import org.goblinframework.api.event.GoblinEventChannel
import org.goblinframework.api.event.GoblinEventContext
import org.goblinframework.api.event.GoblinEventListener
import org.goblinframework.core.container.ContainerRefreshedEvent

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