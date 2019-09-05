package org.goblinframework.core.event

import org.goblinframework.core.concurrent.GoblinFutureImpl
import org.goblinframework.core.concurrent.GoblinFutureResult

class GoblinEventFuture : GoblinFutureImpl<GoblinEventContext>() {

  override fun doResultObtained(result: GoblinFutureResult<GoblinEventContext>) {
    val ctx = result.left!!
    if (ctx.isSuccess) {
      return
    }
    (ctx as? GoblinEventContextImpl)?.run { throwException() }
  }
}
