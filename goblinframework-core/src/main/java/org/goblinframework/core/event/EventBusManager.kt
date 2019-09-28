package org.goblinframework.core.event

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.event.*

@Install
class EventBusManager : IEventBusManager {

  override fun register(channel: String, ringBufferSize: Int, workerHandlers: Int) {
    EventBusBoss.INSTANCE.register(channel, ringBufferSize, workerHandlers)
  }

  override fun unregister(channel: String) {
    EventBusBoss.INSTANCE.unregister(channel)
  }

  override fun subscribe(listener: GoblinEventListener) {
    EventBusBoss.INSTANCE.subscribe(listener)
  }

  override fun subscribe(channel: String, listener: GoblinEventListener) {
    EventBusBoss.INSTANCE.subscribe(channel, listener)
  }

  override fun unsubscribe(listener: GoblinEventListener) {
    EventBusBoss.INSTANCE.unsubscribe(listener)
  }

  override fun unsubscribe(channel: String, listener: GoblinEventListener) {
    EventBusBoss.INSTANCE.unsubscribe(channel, listener)
  }

  override fun publish(event: GoblinEvent): GoblinEventFuture {
    return EventBusBoss.INSTANCE.publish(event)
  }

  override fun publish(channel: String, event: GoblinEvent): GoblinEventFuture {
    return EventBusBoss.INSTANCE.publish(channel, event)
  }

  override fun <E : Any?> execute(callback: GoblinCallback<E>): GoblinCallbackFuture<E> {
    val future = GoblinCallbackFuture<E>()
    val event = GoblinCallbackEvent(callback)
    publish(event).addListener {
      val context: GoblinEventContext
      try {
        context = it.uninterruptibly
      } catch (ex: Throwable) {
        future.complete(null, ex)
        return@addListener
      }
      @Suppress("UNCHECKED_CAST")
      val result = context.getExtension("GoblinCallback.Result") as E?
      future.complete(result)
    }
    return future
  }
}