package org.goblinframework.queue.consumer

import org.goblinframework.queue.api.QueueConsumerMXBean
import java.lang.management.PlatformManagedObject

interface QueueConsumerManagerMXBean : PlatformManagedObject {

  fun getConsumers(): Array<QueueConsumerMXBean>
}