package org.goblinframework.example.queue.producer

import org.goblinframework.queue.GoblinMessageSerializer
import org.goblinframework.queue.QueueSystem
import org.goblinframework.queue.api.GoblinQueueProducer
import org.goblinframework.queue.api.QueueMessageProducer
import javax.inject.Named

@Named
class SampleProducer {

  @GoblinQueueProducer(system = QueueSystem.KFK, config = "default", queue = "test.example.queue")
  private lateinit var kafkaProducer: QueueMessageProducer

  @GoblinQueueProducer(system = QueueSystem.KFK, config = "default", queue = "test.example.json.queue", serializer = GoblinMessageSerializer.JSON)
  private lateinit var kafkaJsonProducer: QueueMessageProducer

  fun getKafkaProducer(): QueueMessageProducer {
    return kafkaProducer
  }

  fun getKafkaJsonProducer(): QueueMessageProducer {
    return kafkaJsonProducer
  }
}