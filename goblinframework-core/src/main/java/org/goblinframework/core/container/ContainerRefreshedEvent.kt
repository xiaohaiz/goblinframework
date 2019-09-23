package org.goblinframework.core.container

import org.goblinframework.api.event.GoblinEvent
import org.goblinframework.api.event.GoblinEventChannel
import org.springframework.context.ApplicationContext

@GoblinEventChannel("/goblin/core")
class ContainerRefreshedEvent(val applicationContext: ApplicationContext) : GoblinEvent()