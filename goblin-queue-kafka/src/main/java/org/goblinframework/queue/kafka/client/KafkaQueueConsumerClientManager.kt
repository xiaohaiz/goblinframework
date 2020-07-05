package org.goblinframework.queue.kafka.client

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.queue.kafka.module.config.KafkaConfig
import org.goblinframework.queue.kafka.module.config.KafkaConfigManager
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
@GoblinManagedBean(type = "Kafka", name = "KafkaQueueConsumerClientManager")
class KafkaQueueConsumerClientManager : GoblinManagedObject(), KafkaQueueConsumerClientManagerMXBean {

  companion object {
    @JvmField val INSTANCE = KafkaQueueConsumerClientManager()
  }

  private val locker = ReentrantReadWriteLock()
  private val buffer = mutableMapOf<ClientTuple, KafkaQueueConsumerClient?>()

  fun getConsumerClient(name: String, group: String): KafkaQueueConsumerClient? {
    val config = KafkaConfigManager.INSTANCE.getKafkaClient(name)
        ?: return null
    return getConsumerClient(config, group)
  }

  fun getConsumerClient(config: KafkaConfig, group: String): KafkaQueueConsumerClient? {
    val tuple = ClientTuple(config, group)
    locker.read { buffer[tuple]?.let { return it } }
    locker.write {
      buffer[tuple]?.let { return it }
      val client = KafkaQueueConsumerClient(tuple.config, tuple.group)
      buffer[tuple] = client
      return client
    }
  }

  override fun getConsumerClients(): Array<KafkaQueueConsumerClientMXBean> {
    locker.read { return buffer.values.mapNotNull { it as KafkaQueueConsumerClientMXBean }.toTypedArray() }
  }

  class ClientTuple (val config: KafkaConfig, val group: String) {
    override fun equals(other: Any?): Boolean {
      if (other !is ClientTuple) {
        return false
      }
      return this.config == other.config && this.group == other.group
    }

    override fun hashCode(): Int {
      return this.config.hashCode() + this.group.hashCode()
    }
  }
}