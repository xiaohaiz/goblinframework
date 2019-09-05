package org.goblinframework.core.conrrent

import org.goblinframework.api.concurrent.GoblinFuture
import org.goblinframework.api.concurrent.GoblinFutureListener

import java.util.concurrent.atomic.AtomicBoolean

internal class GoblinFutureListenerDelegator<T>
internal constructor(private val delegator: GoblinFutureListener<T>)
  : GoblinFutureListener<T> {

  private val executed = AtomicBoolean()

  override fun futureCompleted(future: GoblinFuture<T>) {
    if (executed.compareAndSet(false, true)) {
      delegator.futureCompleted(future)
    }
  }

}
