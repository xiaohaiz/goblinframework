package org.goblinframework.queue.producer

import org.goblinframework.api.core.SerializerMode
import org.goblinframework.core.serialization.SerializerManager
import org.goblinframework.core.util.JsonUtils
import org.goblinframework.queue.GoblinMessage
import org.goblinframework.queue.SendResultFuture
import org.goblinframework.queue.api.QueueMessageProducer

class DefaultQueueMessageProducer(private val producer: DefaultQueueProducer) : QueueMessageProducer {
  override fun send(message: GoblinMessage) {
    sendAsync(message).awaitUninterruptibly()
  }

  override fun sendAsync(message: GoblinMessage): SendResultFuture {
    val data = when(message.serializer) {
      GoblinMessage.GoblinMessageSerializer.JSON -> JsonUtils.toJson(message).toByteArray(Charsets.UTF_8)
      GoblinMessage.GoblinMessageSerializer.HESSIAN2 -> SerializerManager.INSTANCE.getSerializer(SerializerMode.HESSIAN2).serialize(message)
      GoblinMessage.GoblinMessageSerializer.FST -> SerializerManager.INSTANCE.getSerializer(SerializerMode.FST).serialize(message)
      GoblinMessage.GoblinMessageSerializer.JAVA -> SerializerManager.INSTANCE.getSerializer(SerializerMode.JAVA).serialize(message)
      else -> null
    }

    if (data == null) {
      val future = SendResultFuture()
      future.complete(System.currentTimeMillis(), IllegalArgumentException("Serializer not found for message [$message]"))
      return future
    }

    return producer.sendAsync(data)
  }
}