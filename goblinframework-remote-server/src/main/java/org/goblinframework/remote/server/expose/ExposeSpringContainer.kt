package org.goblinframework.remote.server.expose

import org.goblinframework.core.container.ContainerRefreshedEvent
import org.goblinframework.core.event.GoblinEventChannel
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener

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