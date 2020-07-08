package org.goblinframework.queue.kafka.client

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.BytesDeserializer
import org.apache.kafka.common.utils.Bytes
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.queue.kafka.module.config.KafkaConfig
import org.goblinframework.queue.kafka.utils.GoblinIntegerDeserializer
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@GoblinManagedBean(type = "Kafka", name = "KafkaQueueConsumerClient")
class KafkaQueueConsumerClient constructor(private val config: KafkaConfig, private val group: String)
  : GoblinManagedObject(), KafkaQueueConsumerClientMXBean {

  private val consumerFactory: DefaultKafkaConsumerFactory<Int, Bytes>

  init {
    consumerFactory = DefaultKafkaConsumerFactory<Int, Bytes>(properties())
  }

  fun consumerFactory(): DefaultKafkaConsumerFactory<Int, Bytes> {
    return this.consumerFactory
  }

  override fun getConnection(): String {
    return config.getName()
  }

  override fun getGroup(): String {
    return group
  }

  private fun properties(): Map<String, Any> {
    return mapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to config.getServers(),
        ConsumerConfig.GROUP_ID_CONFIG to group,
        ConsumerConfig.MAX_POLL_RECORDS_CONFIG to config.getConsumerMaxPollRecords(),
        ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG to config.getConsumerMaxPollInterval(),
        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to config.isConsumerAutoCommit(),
        ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG to config.getConsumerAutoCommitInterval(),
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to config.getConsumerAutoOffset(),
        ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG to config.getConsumerRequestTimeout(),
        ConsumerConfig.RECEIVE_BUFFER_CONFIG to config.getConsumerBufferSize(),

        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to GoblinIntegerDeserializer::class.java,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to BytesDeserializer::class.java
    )
  }
}