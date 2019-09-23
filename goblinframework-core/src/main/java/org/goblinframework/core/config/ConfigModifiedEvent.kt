package org.goblinframework.core.config

import org.goblinframework.api.event.GoblinEvent
import org.goblinframework.api.event.GoblinEventChannel

@GoblinEventChannel("/goblin/core")
class ConfigModifiedEvent : GoblinEvent()