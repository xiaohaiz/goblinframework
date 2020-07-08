package org.goblinframework.queue.kafka.client

interface KafkaQueueConsumerClientMXBean {

  fun getConnection(): String

  fun getGroup(): String
}