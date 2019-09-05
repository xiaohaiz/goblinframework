package org.goblinframework.core.concurrent

import org.apache.commons.lang3.tuple.Pair

internal class GoblinFutureResult<T>
internal constructor(private val ret: T?, private val cause: Throwable?)
  : Pair<T, Throwable>() {

  override fun getLeft(): T? {
    return ret
  }

  override fun getRight(): Throwable? {
    return cause
  }

  override fun setValue(newValue: Throwable): Throwable {
    throw UnsupportedOperationException()
  }
}
