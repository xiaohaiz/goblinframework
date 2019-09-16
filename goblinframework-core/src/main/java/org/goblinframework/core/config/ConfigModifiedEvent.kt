package org.goblinframework.core.config

import org.goblinframework.core.event.GoblinEvent
import org.goblinframework.core.event.GoblinEventChannel

@GoblinEventChannel("/goblin/core")
class ConfigModifiedEvent : GoblinEvent()