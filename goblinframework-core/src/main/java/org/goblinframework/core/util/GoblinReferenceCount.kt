package org.goblinframework.core.util

import org.goblinframework.api.core.ReferenceCount
import java.util.concurrent.atomic.AtomicInteger

class GoblinReferenceCount(initialize: Int = 0) : ReferenceCount {

  private val referenceCount: AtomicInteger

  init {
    if (initialize < 0) {
      throw IllegalArgumentException()
    }
    referenceCount = AtomicInteger(initialize)
  }

  override fun count(): Int {
    return referenceCount.get()
  }

  override fun retain() {
    referenceCount.incrementAndGet()
  }

  override fun retain(increment: Int) {
    referenceCount.addAndGet(increment)
  }

  override fun release(): Boolean {
    return referenceCount.decrementAndGet() <= 0
  }

  override fun release(decrement: Int): Boolean {
    return referenceCount.addAndGet(-decrement) <= 0
  }
}