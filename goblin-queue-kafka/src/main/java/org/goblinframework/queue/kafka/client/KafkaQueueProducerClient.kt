package org.goblinframework.queue.kafka.client

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.BytesSerializer
import org.apache.kafka.common.serialization.IntegerSerializer
import org.apache.kafka.common.utils.Bytes
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.queue.kafka.module.config.KafkaConfig
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import java.util.concurrent.atomic.AtomicBoolean

@GoblinManagedBean("Kafka", name = "KafkaQueueProducerClient")
class KafkaQueueProducerClient internal constructor(config: KafkaConfig)
  : GoblinManagedObject(), KafkaQueueProducerClientMXBean {

  val config: KafkaConfig = config

  val producerFactory: DefaultKafkaProducerFactory<Int, Bytes>
  val kafkaTemplate: KafkaTemplate<Int, Bytes>

  private val shutdown = AtomicBoolean(false)

  init {
    // 一个config对应一个producer factory
    producerFactory = DefaultKafkaProducerFactory(properties())
    // 一个producer factory对应一个KafkaTemplate
    kafkaTemplate = KafkaTemplate(producerFactory)
  }

  fun asyncSend(): Boolean {
    return config.isProducerAsyncSend()
  }

  override fun getConnection(): String {
    return config.getName()
  }

  override fun shutdown() {
    if (shutdown.compareAndSet(false, true)) {
      try {
        producerFactory.destroy()
        logger.info("Kafka producer factory shutdown: {}", producerFactory)
      } catch (e: Exception) {
        logger.error("Kafka producer client shutdown failed: {}", e)
      }
    }
  }

  private fun properties(): Map<String, Any?> {
    return mapOf<String, Any?>(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to config.getServers(),
        ProducerConfig.BATCH_SIZE_CONFIG to config.getProducerBatchSize(),
        ProducerConfig.ACKS_CONFIG to config.getProducerAcks(),
        ProducerConfig.LINGER_MS_CONFIG to config.getProducerLingerMs(),
        ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG to config.getProducerRequestTimeout(),
        ProducerConfig.MAX_REQUEST_SIZE_CONFIG to config.getProducerMaxRequestSize(),
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to IntegerSerializer::class.java,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to BytesSerializer::class.java
    )
  }
}