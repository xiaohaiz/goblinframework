package org.goblinframework.core.event.callback

import org.goblinframework.api.event.GoblinCallbackException
import org.goblinframework.api.event.GoblinEventChannel
import org.goblinframework.api.event.GoblinEventContext
import org.goblinframework.api.event.GoblinEventListener

@GoblinEventChannel("/goblin/core")
class GoblinCallbackEventListener private constructor() : GoblinEventListener {

  companion object {
    val INSTANCE = GoblinCallbackEventListener()
  }

  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is GoblinCallbackEvent
  }

  override fun onEvent(context: GoblinEventContext) {
    val event = context.event as GoblinCallbackEvent
    val callback = event.callback
    try {
      callback.call()?.run {
        context.setExtension("GoblinCallback.Result", this)
      }

    } catch (ex: Throwable) {
      GoblinCallbackException.throwException(ex)
    }

  }
}
