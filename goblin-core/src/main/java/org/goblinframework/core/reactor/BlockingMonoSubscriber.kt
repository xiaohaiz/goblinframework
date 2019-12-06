package org.goblinframework.core.reactor

import org.goblinframework.api.function.Disposable
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import reactor.core.publisher.MonoProcessor

class BlockingMonoSubscriber<T> : Subscriber<T>, Disposable {

  private val mono = MonoProcessor.create<T>()

  override fun onSubscribe(s: Subscription) {
    mono.onSubscribe(s)
  }

  override fun onNext(t: T?) {
    mono.onNext(t)
  }

  override fun onError(t: Throwable) {
    mono.onError(t)
  }

  override fun onComplete() {
    mono.onComplete()
  }

  override fun dispose() {
    mono.dispose()
  }

  fun subscribe(publisher: Publisher<T>): BlockingMonoSubscriber<T> {
    publisher.subscribe(this)
    return this
  }

  fun block(): T? {
    return mono.block()
  }


}