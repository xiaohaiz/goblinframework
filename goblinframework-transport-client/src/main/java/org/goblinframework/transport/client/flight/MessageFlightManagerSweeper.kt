package org.goblinframework.transport.client.flight

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.event.GoblinEventContext
import org.goblinframework.core.event.MinuteTimerEventListener

@Singleton
class MessageFlightManagerSweeper private constructor() : MinuteTimerEventListener() {

  companion object {
    @JvmField val INSTANCE = MessageFlightManagerSweeper()
  }

  override fun periodMinutes(): Long {
    return 30
  }

  override fun onEvent(context: GoblinEventContext) {
    MessageFlightManager.INSTANCE.clearExpired()
  }
}