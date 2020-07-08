package org.goblinframework.queue.producer.builder

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.queue.GoblinQueueException
import org.goblinframework.queue.QueueSystem
import java.util.concurrent.ConcurrentHashMap

@Singleton
@ThreadSafe
@GoblinManagedBean(type = "queue")
class QueueProducerBuilderManager private constructor()
  : GoblinManagedObject(), QueueProducerBuilderManagerMXBean {

  companion object {
    @JvmField
    val INSTANCE = QueueProducerBuilderManager()
  }

  private val buffer = ConcurrentHashMap<QueueSystem, QueueProducerBuilderDelegator>()

  override fun getQueueProducerBuilderList(): Array<QueueProducerBuilderMXBean> {
    return buffer.values
        .sortedBy { it.system() }
        .toTypedArray()
  }

  @Synchronized
  fun register(builder: QueueProducerBuilder) {
    val system = builder.system()
    buffer[system]?.run {
      throw GoblinQueueException("Queue system $system already exists")
    }
    buffer[system] = QueueProducerBuilderDelegator(builder)
  }

  fun builder(system: QueueSystem): QueueProducerBuilder? {
    return buffer[system]
  }

  override fun disposeBean() {
    buffer.values.forEach { it.dispose() }
    buffer.clear()
  }


}