package org.goblinframework.core.reactor

import org.goblinframework.api.function.Disposable
import org.goblinframework.core.concurrent.GoblinInterruptedException
import org.goblinframework.core.util.ExceptionUtils
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.CountDownLatch

class BlockingListSubscriber<T> : CountDownLatch(1), Subscriber<T>, Disposable {

  private val values = mutableListOf<T>()
  private var error: Throwable? = null
  private var subscription: Subscription? = null
  private var cancelled = false

  override fun onSubscribe(s: Subscription) {
    this.subscription = s
    if (!cancelled) {
      s.request(Long.MAX_VALUE)
    }
  }

  override fun onNext(t: T?) {
    t?.run { values.add(this) }
  }

  override fun onError(t: Throwable) {
    values.clear()
    error = t
    countDown()
  }

  override fun onComplete() {
    countDown()
  }

  override fun dispose() {
    cancelled = true
    val s = this.subscription
    s?.run {
      this@BlockingListSubscriber.subscription = null
      this.cancel()
    }
  }

  fun block(): List<T> {
    if (count.toInt() != 0) {
      try {
        await()
      } catch (ex: InterruptedException) {
        dispose()
        throw GoblinInterruptedException(ex)
      }
    }
    error?.run {
      val cause = ExceptionUtils.propagate(this)
      cause.addSuppressed(Exception("#block terminated with an error"))
      throw cause
    }
    return values.toList()
  }

}