package org.goblinframework.example.queue.producer

import org.goblinframework.queue.QueueSystem
import org.goblinframework.queue.api.GoblinQueueProducer
import org.goblinframework.queue.api.QueueMessageProducer
import javax.inject.Named

@Named
class SampleProducer {

  @GoblinQueueProducer(system = QueueSystem.KFK, config = "default", queue = "test.example.queue")
  private lateinit var kafkaProducer: QueueMessageProducer

  fun getKafkaProducer(): QueueMessageProducer {
    return kafkaProducer
  }
}