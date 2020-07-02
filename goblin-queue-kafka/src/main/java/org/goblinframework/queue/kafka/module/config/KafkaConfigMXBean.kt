package org.goblinframework.queue.kafka.module.config

import java.lang.management.PlatformManagedObject

interface KafkaConfigMXBean : PlatformManagedObject {
    fun getName(): String

    fun getServers(): String

    fun isProducerAsyncSend(): Boolean

    fun getProducerBatchSize(): Int

    fun getProducerAcks(): String

    fun getProducerRequestTimeout(): Int

    fun getProducerLingerMs(): Long

    fun getProducerMaxRequestSize(): Int

    fun isConsumerAutoCommit(): Boolean

    fun getConsumerAutoCommitInterval(): Int

    fun getConsumerMaxPollRecords(): Int

    fun getConsumerMaxPollInterval(): Int

    fun getConsumerAutoOffset(): String

    fun getConsumerRequestTimeout(): Int

    fun getConsumerBufferSize(): Int
}