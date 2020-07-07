package org.goblinframework.queue.producer.builder

import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.queue.QueueSystem
import org.goblinframework.queue.api.QueueProducer
import org.goblinframework.queue.api.QueueProducerMXBean
import org.goblinframework.queue.producer.QueueProducerDefinition
import org.goblinframework.queue.producer.QueueProducerDelegator
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * Delegator for queue producer builder
 * Default behavior implemented
 */
class QueueProducerBuilderDelegator
internal constructor(private val delegator: QueueProducerBuilder)
  : GoblinManagedObject(), QueueProducerBuilder, QueueProducerBuilderMXBean {

  private val lock = ReentrantReadWriteLock()
  private val buffer = ConcurrentHashMap<String, QueueProducerDelegator?>()


  override fun getSystem(): QueueSystem {
    return system()
  }

  override fun getProducerList(): Array<QueueProducerMXBean> {
    return buffer.values
        .mapNotNull { it }
        .toTypedArray()
  }

  override fun producer(definition: QueueProducerDefinition): QueueProducer? {
    val name = definition.location.toString()

    lock.read { buffer[name]?.let { return it } }
    lock.write {
      buffer[name]?.let { return it }
      val producer = delegator.producer(definition)?.let { QueueProducerDelegator(it) }
      buffer[name] = producer
      return producer
    }
  }

  override fun system(): QueueSystem {
    return delegator.system()
  }

  override fun disposeBean() {
    buffer.values.mapNotNull { it }.forEach { it.dispose() }
    buffer.clear()
  }
}