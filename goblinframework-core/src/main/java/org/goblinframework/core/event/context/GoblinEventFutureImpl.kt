package org.goblinframework.core.event.context

import org.goblinframework.api.concurrent.GoblinFuture
import org.goblinframework.core.concurrent.GoblinFutureImpl
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventFuture
import org.goblinframework.core.event.exception.EventBusException

class GoblinEventFutureImpl : GoblinFutureImpl<GoblinEventContext>(), GoblinEventFuture {

  override fun complete(result: GoblinEventContext?): GoblinFuture<GoblinEventContext> {
    val context = result as GoblinEventContextImpl
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

}
