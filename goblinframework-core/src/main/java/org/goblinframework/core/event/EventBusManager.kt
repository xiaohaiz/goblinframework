package org.goblinframework.core.event

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.event.*
import org.goblinframework.core.event.boss.EventBusBoss
import org.goblinframework.core.event.dsl.GoblinCallbackEvent

@Install
class EventBusManager : IEventBusManager {

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