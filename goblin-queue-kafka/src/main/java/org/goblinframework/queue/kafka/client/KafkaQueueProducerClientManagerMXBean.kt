package org.goblinframework.queue.kafka.client

import java.lang.management.PlatformManagedObject

interface KafkaQueueProducerClientManagerMXBean: PlatformManagedObject {

  fun getClients(): Array<KafkaQueueProducerClientMXBean>

  fun shutdown()
}