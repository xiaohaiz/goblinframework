package org.goblinframework.core.config

import org.goblinframework.api.event.EventBus
import org.goblinframework.api.event.GoblinEventContext
import org.goblinframework.core.event.dsl.MinuteTimerEventListener

class ConfigLoaderScheduler internal constructor(private val configLoader: ConfigLoader)
  : MinuteTimerEventListener() {

  override fun onEvent(context: GoblinEventContext) {
    if (configLoader.reload()) {
      EventBus.publish(ConfigModifiedEvent())
    }
  }
}