package org.goblinframework.core.event

import org.goblinframework.core.concurrent.GoblinFutureImpl
import org.goblinframework.core.concurrent.GoblinFutureResult

class GoblinEventFuture : GoblinFutureImpl<GoblinEventContext>() {

  override fun doResultObtained(result: GoblinFutureResult<GoblinEventContext>) {
    val ctx = result.result!!
    if (ctx.isSuccess) {
      return
    }
    if (ctx.event.isRaiseException) {
      ctx.throwException()
    } else {
      result.cause = null;
    }
  }
}
