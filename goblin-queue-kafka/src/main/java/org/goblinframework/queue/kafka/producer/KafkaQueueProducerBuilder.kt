package org.goblinframework.queue.kafka.producer

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.queue.QueueSystem
import org.goblinframework.queue.api.QueueProducer
import org.goblinframework.queue.producer.QueueProducerDefinition
import org.goblinframework.queue.producer.builder.QueueProducerBuilder
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
class KafkaQueueProducerBuilder : GoblinManagedObject(), QueueProducerBuilder {

  companion object {
    @JvmField val INSTANCE = KafkaQueueProducerBuilder()
  }

  private val locker = ReentrantReadWriteLock()
  private val buffer = mutableMapOf<QueueProducerDefinition, KafkaQueueProducer>()

  override fun system(): QueueSystem {
    return QueueSystem.KFK
  }

  override fun producer(definition: QueueProducerDefinition): QueueProducer? {
    locker.read {
      buffer[definition]
    }?.let { return it }
    locker.write {
      buffer[definition]?.let { return it }
      buffer[definition] = KafkaQueueProducer(definition)
      return buffer[definition]
    }
  }
}