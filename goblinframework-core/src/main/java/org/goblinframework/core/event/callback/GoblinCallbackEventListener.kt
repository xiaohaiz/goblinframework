package org.goblinframework.core.event.callback

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.event.GoblinEventChannel
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener

@Singleton
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

  @Install
  @GoblinEventChannel("/goblin/core")
  class Installer : GoblinEventListener by INSTANCE
}
