package org.goblinframework.queue.kafka.producer

import org.apache.kafka.common.utils.Bytes
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.queue.SendResultFuture
import org.goblinframework.queue.api.QueueProducer
import org.goblinframework.queue.api.QueueProducerMXBean
import org.goblinframework.queue.kafka.client.KafkaQueueProducerClientManager
import org.goblinframework.queue.producer.QueueProducerDefinition
import org.springframework.kafka.support.SendResult
import org.springframework.util.concurrent.ListenableFutureCallback
import java.util.concurrent.atomic.AtomicLong

@GoblinManagedBean(type = "Kafka", name = "KafkaQueueProducer")
class KafkaQueueProducer constructor(definition: QueueProducerDefinition) : GoblinManagedObject(), QueueProducer, QueueProducerMXBean {

  private val definition: QueueProducerDefinition = definition

  private val successCount = AtomicLong(0)
  private val failedCount = AtomicLong(0)

  override fun sendAsync(data: ByteArray?): SendResultFuture {
    // 到这层不支持异步了
    // 异步是在DefaultQueueProducer里，通过EventBus处理的，这里只是兼容下接口
    val result = SendResultFuture(1)

    if (data == null) {
      result.complete(1)
      return result
    }

    val client = KafkaQueueProducerClientManager.INSTANCE.getClient(definition.location.config)
        ?: throw IllegalArgumentException("No kafka client found for ${definition.location.config}")

    val template = client.kafkaTemplate
    val ret = template.send(definition.location.queue, Bytes(data))

    ret.addCallback(object: ListenableFutureCallback<SendResult<Int, Bytes>> {
      override fun onSuccess(p0: SendResult<Int, Bytes>?) {
        result.complete(1)
      }

      override fun onFailure(p0: Throwable) {
        result.complete(1)
      }
    })

    if (!client.asyncSend()) {
      template.flush()
      ret.get()
    }

    return result
  }

  override fun send(data: ByteArray?) {
    sendAsync(data).awaitUninterruptibly()
  }

  override fun getSuccessCount(): Long {
    return successCount.get()
  }

  override fun getDefinition(): String {
    return definition.toString()
  }

  override fun getProducerType(): String {
    return "KafkaProducer"
  }

  override fun getFailedCount(): Long {
    return failedCount.get()
  }

  override fun produceText(text: String?) {
    send(text?.toByteArray(Charsets.UTF_8))
  }

}