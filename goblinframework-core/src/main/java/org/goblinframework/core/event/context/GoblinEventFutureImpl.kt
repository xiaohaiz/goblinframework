package org.goblinframework.core.event.context

import org.goblinframework.api.concurrent.GoblinFuture
import org.goblinframework.api.function.Block0
import org.goblinframework.core.concurrent.GoblinFutureImpl
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventFuture
import org.goblinframework.core.event.exception.EventBusException

class GoblinEventFutureImpl : GoblinFutureImpl<GoblinEventContext>(), GoblinEventFuture {

  private var context: GoblinEventContextImpl? = null

  override fun complete(result: GoblinEventContext?): GoblinFuture<GoblinEventContext> {
    val context = result as GoblinEventContextImpl
    this.context = context
    if (context.isSuccess) {
      return super.complete(result)
    }
    if (!context.event.isRaiseException) {
      return super.complete(result)
    }
    var error: EventBusException? = null
    context.throwExceptionIfNecessary()?.run {
      error = this
    }
    return super.complete(result, error)
  }

  override fun addDiscardListener(action: Block0) {
    addListener {
      context?.run {
        if (this.isDiscard) {
          action.apply()
        }
      }
    }
  }
}
