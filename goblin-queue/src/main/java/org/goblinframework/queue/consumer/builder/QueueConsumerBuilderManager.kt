package org.goblinframework.queue.consumer.builder

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.queue.GoblinQueueException
import org.goblinframework.queue.QueueSystem
import java.util.concurrent.ConcurrentHashMap

@Singleton
@ThreadSafe
@GoblinManagedBean(type = "Queue")
class QueueConsumerBuilderManager private constructor()
  : GoblinManagedObject(), QueueConsumerBuilderManagerMXBean {

  companion object {
    @JvmField val INSTANCE = QueueConsumerBuilderManager()
  }

  private val buffer = ConcurrentHashMap<QueueSystem, QueueConsumerBuilderDelegator>()

  override fun getQueueConsumerBuilderList(): Array<QueueConsumerBuilderMXBean> {
    return buffer.values.sortedBy { it.system() }.toTypedArray()
  }

  @Synchronized
  fun register(builder: QueueConsumerBuilder) {
    val system = builder.system()
    buffer[system]?.run {
      throw GoblinQueueException("Queue system $system already exists")
    }
    buffer[system] = QueueConsumerBuilderDelegator((builder))
  }

  fun builder(system: QueueSystem): QueueConsumerBuilder? {
    return buffer[system]
  }

  override fun disposeBean() {
    buffer.values.forEach { it.dispose() }
    buffer.clear()
  }
}