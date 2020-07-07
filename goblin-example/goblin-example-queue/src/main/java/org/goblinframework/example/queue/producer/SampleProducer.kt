package org.goblinframework.example.queue.producer

import org.goblinframework.queue.GoblinMessageSerializer
import org.goblinframework.queue.QueueSystem
import org.goblinframework.queue.api.GoblinQueueProducer
import org.goblinframework.queue.api.GoblinQueueProducers
import org.goblinframework.queue.api.QueueMessageProducer
import javax.inject.Named

@Named
class SampleProducer {

  @GoblinQueueProducers(
      GoblinQueueProducer(system = QueueSystem.KFK, config = "default", queue = "test.example.queue"),
      GoblinQueueProducer(system = QueueSystem.KFK, config = "default", queue = "test.example.json.queue", serializer = GoblinMessageSerializer.JSON)
  )
  private lateinit var kafkaProducer: QueueMessageProducer

  fun getKafkaProducer(): QueueMessageProducer {
    return kafkaProducer
  }
}