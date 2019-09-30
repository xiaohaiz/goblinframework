package org.goblinframework.core.reactor

import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicReference

class BlockingListSubscriber<T> : Subscriber<T> {

  private val latch = CountDownLatch(1)
  private var subscription: Subscription? = null
  private val cause = AtomicReference<Throwable?>()
  private val values = mutableListOf<T>()

  override fun onSubscribe(s: Subscription?) {
    this.subscription = s
    this.subscription?.request(1)
  }

  override fun onNext(t: T) {
    values.add(t)
    this.subscription?.request(1)
  }

  override fun onError(t: Throwable?) {
    cause.set(t)
    latch.countDown()
  }

  override fun onComplete() {
    latch.countDown()
  }

  fun block(): List<T> {
    latch.await()
    cause.get()?.run { throw this }
    return values.toList()
  }
}