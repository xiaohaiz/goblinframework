package org.goblinframework.queue.kafka.client

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.queue.kafka.module.config.KafkaConfig
import org.goblinframework.queue.kafka.module.config.KafkaConfigManager
import java.util.concurrent.ConcurrentHashMap

@Singleton
@GoblinManagedBean(type = "Kafka", name = "KafkaQueueProducerClientManager")
class KafkaQueueProducerClientManager : GoblinManagedObject(), KafkaQueueProducerClientManagerMXBean {

  companion object {
    @JvmField
    val INSTANCE = KafkaQueueProducerClientManager()
  }

  private val buffer = ConcurrentHashMap<KafkaConfig, KafkaQueueProducerClient>()

  fun getClient(config: KafkaConfig): KafkaQueueProducerClient? {
    return buffer.computeIfAbsent(config) { KafkaQueueProducerClient(config) }
  }

  fun getClient(config: String): KafkaQueueProducerClient? {
    val kafkaConfig = KafkaConfigManager.INSTANCE.getKafkaClient(config)
        ?: return null
    return getClient(kafkaConfig)
  }

  override fun getClients(): Array<KafkaQueueProducerClientMXBean> {
    return buffer.values.sortedBy { it.config.getName() }.toTypedArray()
  }

  override fun shutdown() {
    buffer.values.parallelStream().forEach { it.shutdown() }
  }
}