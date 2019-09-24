package org.goblinframework.core.config

import org.goblinframework.api.event.EventBus
import org.goblinframework.api.event.GoblinEventContext
import org.goblinframework.core.event.dsl.MinuteTimerEventListener

@Deprecated("TBR")
class ConfigLoaderScheduler1 internal constructor(private val configLoader: ConfigLoader)
  : MinuteTimerEventListener() {

  override fun onEvent(context: GoblinEventContext) {
    if (configLoader.reload()) {
      EventBus.publish(ConfigModifiedEvent())
    }
  }
}