package org.goblinframework.core.event.context

import org.goblinframework.api.concurrent.GoblinFuture
import org.goblinframework.core.concurrent.GoblinFutureImpl
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventContextImpl
import org.goblinframework.core.event.GoblinEventFuture
import org.goblinframework.core.event.exception.EventBusException

class GoblinEventFutureImpl : GoblinFutureImpl<GoblinEventContext>(), GoblinEventFuture {

  override fun complete(result: GoblinEventContext?): GoblinFuture<GoblinEventContext> {
    var error: EventBusException? = null
    (result as? GoblinEventContextImpl)?.run {
      error = this.throwExceptionIfNecessary()
    }
    return super.complete(result, error)
  }

  override fun complete(result: GoblinEventContext?, cause: Throwable?): GoblinFuture<GoblinEventContext> {
    throw UnsupportedOperationException()
  }

}
