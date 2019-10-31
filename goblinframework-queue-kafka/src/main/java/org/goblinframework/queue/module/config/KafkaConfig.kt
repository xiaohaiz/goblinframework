package org.goblinframework.queue.module.config

import org.goblinframework.core.config.GoblinConfig
import org.goblinframework.core.service.GoblinManagedObject

class KafkaConfig internal constructor(val mapper: KafkaConfigMapper)
    : GoblinManagedObject(), GoblinConfig, KafkaConfigMXBean {

    override fun getName(): String {
        return mapper.name!!
    }

    override fun getServers(): String {
        return mapper.servers!!
    }

    override fun isProducerAsyncSend(): Boolean {
        return mapper.producer!!.asyncSend!!
    }

    override fun getProducerBatchSize(): Int {
        return mapper.producer!!.batchSize!!
    }

    override fun getProducerAcks(): String {
        return mapper.producer!!.acks!!
    }

    override fun getProducerRequestTimeout(): Int {
        return mapper.producer!!.requestTimeout!!
    }

    override fun getProducerLingerMs(): Long {
        return mapper.producer!!.lingerMs!!
    }

    override fun getProducerMaxRequestSize(): Int {
        return mapper.producer!!.maxRequestSize!!
    }

    override fun isConsumerAutoCommit(): Boolean {
        return mapper.consumer!!.autoCommit!!
    }

    override fun getConsumerAutoCommitInterval(): Int {
        return mapper.consumer!!.autoCommitInterval!!
    }

    override fun getConsumerMaxPollRecords(): Int {
        return mapper.consumer!!.maxPollRecords!!
    }

    override fun getConsumerMaxPollInterval(): Int {
        return mapper.consumer!!.maxPollInterval!!
    }

    override fun getConsumerAutoOffset(): String {
        return mapper.consumer!!.autoOffset!!
    }

    override fun getConsumerRequestTimeout(): Int {
        return mapper.consumer!!.requestTimeout!!
    }

    override fun getConsumerBufferSize(): Int {
        return mapper.consumer!!.bufferSize!!
    }
}