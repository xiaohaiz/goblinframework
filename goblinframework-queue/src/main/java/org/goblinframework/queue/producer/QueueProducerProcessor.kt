package org.goblinframework.queue.producer

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.container.SpringContainerBeanPostProcessor
import org.goblinframework.core.util.CollectionUtils
import org.goblinframework.core.util.GoblinField
import org.goblinframework.core.util.ReflectionUtils

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
            definitions.forEach { def ->
              val builder = QueueProducerBuilderManager.INSTANCE.builder(def.location.queueSystem)
                  ?: throw IllegalArgumentException("Queue system ${def.location.queueSystem} not installed")

              val producer = builder.producer(def)
                  ?: throw  IllegalArgumentException("Producer ${def.location} build failed")


            }
          }
        }

    return bean
  }
}