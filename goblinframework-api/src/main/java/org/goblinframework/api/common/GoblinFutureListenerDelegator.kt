package org.goblinframework.api.common

import java.util.concurrent.atomic.AtomicBoolean

internal class GoblinFutureListenerDelegator<T>
internal constructor(val delegator: GoblinFutureListener<T>, val order: Int)
  : GoblinFutureListener<T> {

  private val executed = AtomicBoolean()

  override fun futureCompleted(future: GoblinFuture<T>) {
    if (executed.compareAndSet(false, true)) {
      delegator.futureCompleted(future)
    }
  }

}
