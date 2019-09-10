package org.goblinframework.core.container

import org.goblinframework.core.event.GoblinEvent
import org.goblinframework.core.event.GoblinEventChannel
import org.springframework.context.ApplicationContext

@GoblinEventChannel("/goblin/core")
class ContainerRefreshedEvent(val applicationContext: ApplicationContext) : GoblinEvent()