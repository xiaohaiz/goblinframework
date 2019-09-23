package org.goblinframework.core.event

import org.goblinframework.api.event.GoblinEventContext
import org.goblinframework.core.concurrent.GoblinFutureImpl
import org.goblinframework.core.concurrent.GoblinFutureResult
import org.goblinframework.core.event.context.GoblinEventContextImpl

class GoblinEventFuture : GoblinFutureImpl<GoblinEventContext>() {

  override fun doResultObtained(result: GoblinFutureResult<GoblinEventContext>) {
    val ctx = result.left!!
    if (ctx.isSuccess) {
      return
    }
    if (ctx.event.isRaiseException) {
      (ctx as? GoblinEventContextImpl)?.run { throwException() }
    }
  }
}
