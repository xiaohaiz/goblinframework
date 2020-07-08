package org.goblinframework.queue.consumer.builder

import org.goblinframework.queue.QueueSystem
import org.goblinframework.queue.api.QueueConsumerMXBean
import java.lang.management.PlatformManagedObject

interface QueueConsumerBuilderMXBean : PlatformManagedObject {
  fun getSystem(): QueueSystem

  fun getConsumerList(): Array<QueueConsumerMXBean>
}