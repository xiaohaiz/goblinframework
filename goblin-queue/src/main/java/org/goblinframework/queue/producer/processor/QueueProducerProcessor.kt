package org.goblinframework.queue.producer.processor

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.container.SpringContainerBeanPostProcessor
import org.goblinframework.core.util.CollectionUtils
import org.goblinframework.core.util.GoblinField
import org.goblinframework.core.util.ReflectionUtils
import org.goblinframework.queue.api.QueueMessageProducer
import org.goblinframework.queue.api.QueueProducer
import org.goblinframework.queue.producer.DefaultQueueMessageProducer
import org.goblinframework.queue.producer.DefaultQueueProducer
import org.goblinframework.queue.producer.QueueProducerDefinitionBuilder
import org.goblinframework.queue.producer.QueueProducerTuple
import org.goblinframework.queue.producer.builder.QueueProducerBuilderManager

@Singleton
class QueueProducerProcessor private constructor() : SpringContainerBeanPostProcessor {

  companion object {
    @JvmField
    val INSTANCE = QueueProducerProcessor()
  }

  override fun postProcessAfterInitialization(bean: Any?, beanName: String?): Any? {
    return bean?.run { tryQueueProducerInjection(bean) }
  }

  private fun tryQueueProducerInjection(bean: Any): Any {
    ReflectionUtils.allFieldsIncludingAncestors(bean.javaClass, false, false)
        .map { GoblinField(it) }
        .forEach {
          val definitions = QueueProducerDefinitionBuilder.build(it)
          if (!CollectionUtils.isEmpty(definitions)) {
            val producerTuples = mutableListOf<QueueProducerTuple>()
            definitions.forEach { def ->
              val builder = QueueProducerBuilderManager.INSTANCE.builder(def.location.queueSystem)
                  ?: throw IllegalArgumentException("Queue system ${def.location.queueSystem} not installed")

              val producer = builder.producer(def)
                  ?: throw  IllegalArgumentException("Producer ${def.location} build failed")

              val producerTuple = QueueProducerTuple(producer, def)
              producerTuples.add(producerTuple)
            }

            when (it.fieldType) {
              QueueMessageProducer::class.java -> {
                val queueProducer = DefaultQueueProducer(producerTuples)
                it.set(bean, DefaultQueueMessageProducer(queueProducer))
              }
              QueueProducer::class.java -> {
                it.set(bean, DefaultQueueProducer(producerTuples))
              }
              else -> throw  IllegalArgumentException("Producer $bean must be QueueMessageProducer or QueueProducer")
            }
          }
        }

    return bean
  }
}