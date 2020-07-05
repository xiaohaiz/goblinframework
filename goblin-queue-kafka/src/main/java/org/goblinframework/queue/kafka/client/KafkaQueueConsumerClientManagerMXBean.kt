package org.goblinframework.queue.kafka.client

import java.lang.management.PlatformManagedObject

interface KafkaQueueConsumerClientManagerMXBean : PlatformManagedObject {
  fun getConsumerClients(): Array<KafkaQueueConsumerClientMXBean>
}