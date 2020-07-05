package org.goblinframework.queue.consumer.processor

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.container.ContainerRefreshedEvent
import org.goblinframework.core.event.GoblinEventChannel
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener
import org.goblinframework.queue.GoblinQueueException
import org.goblinframework.queue.api.QueueMessageListener
import org.goblinframework.queue.consumer.QueueConsumerDefinitionBuilder
import org.goblinframework.queue.consumer.QueueConsumerManager
import org.goblinframework.queue.consumer.builder.QueueConsumerBuilderManager

@Singleton
@GoblinEventChannel("/goblin/core")
class QueueListenerProcessor : GoblinEventListener {
  companion object {
    @JvmField val INSTANCE = QueueListenerProcessor()
  }

  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is ContainerRefreshedEvent
  }

  override fun onEvent(context: GoblinEventContext) {
    val event = context.event as ContainerRefreshedEvent
    val applicationContext = event.applicationContext

    val queueMessageConsumerBeans = applicationContext.getBeanNamesForType(QueueMessageListener::class.java, true, false)
    queueMessageConsumerBeans.forEach {
      val beanType = applicationContext.getType(it)

      val definitions = QueueConsumerDefinitionBuilder.build(beanType)
      if (definitions.isNullOrEmpty()) {
        return@forEach
      }

      definitions.filterNotNull().forEach { definition ->
        val system = definition.location.queueSystem
        val builder = QueueConsumerBuilderManager.INSTANCE.builder(system)
            ?: throw GoblinQueueException("Queue system $system not installed")

        val consumer = builder.consumer(definition, beanType)
            ?: throw GoblinQueueException("Build consumer $definition failed")

        QueueConsumerManager.INSTANCE.register(consumer)
      }
    }
  }
}