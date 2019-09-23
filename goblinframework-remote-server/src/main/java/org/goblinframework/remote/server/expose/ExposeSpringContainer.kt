package org.goblinframework.remote.server.expose

import org.goblinframework.api.event.GoblinEventChannel
import org.goblinframework.api.event.GoblinEventContext
import org.goblinframework.api.event.GoblinEventListener
import org.goblinframework.core.container.ContainerRefreshedEvent

@GoblinEventChannel("/goblin/core")
class ExposeSpringContainer : GoblinEventListener {

  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is ContainerRefreshedEvent
  }

  override fun onEvent(context: GoblinEventContext) {
    val event = context.event as ContainerRefreshedEvent
    ExposeServiceScanner.expose(event.applicationContext)
  }
}