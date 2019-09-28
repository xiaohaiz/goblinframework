package org.goblinframework.api.event

import org.goblinframework.api.core.GoblinFutureImpl
import org.goblinframework.api.core.GoblinFutureResult

class GoblinEventFuture : GoblinFutureImpl<GoblinEventContext>() {

  override fun doResultObtained(result: GoblinFutureResult<GoblinEventContext>) {
    val ctx = result.result!!
    if (ctx.isSuccess) {
      return
    }
    if (ctx.event.isRaiseException) {
      ctx.throwException()
    }
  }
}
