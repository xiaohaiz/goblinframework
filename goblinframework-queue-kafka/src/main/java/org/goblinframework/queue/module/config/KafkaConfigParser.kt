package org.goblinframework.queue.module.config

import org.goblinframework.core.config.BufferedConfigParser
import org.goblinframework.core.config.ConfigManager
import org.goblinframework.core.config.GoblinConfigException
import org.goblinframework.core.util.StringUtils

class KafkaConfigParser internal constructor()
    : BufferedConfigParser<KafkaConfig>() {

    companion object {
        private const val DEFAULT_KAFKA_PORT = 9092
    }

    override fun initializeBean() {
        val mapping = ConfigManager.INSTANCE.getMapping()
        parseToMap(mapping, "kafka", KafkaConfigMapper::class.java)
                .map { it.value.also { c -> c.name = it.key } }
                .map { KafkaConfig(it) }
                .forEach { putIntoBuffer(it.getName(), it) }
    }

    override fun doProcessConfig(config: KafkaConfig) {
        val mapper = config.mapper
        mapper.servers ?: kotlin.run {
            throw GoblinConfigException("kafka.servers is required")
        }

        mapper.servers = StringUtils.formalizeServers(mapper.servers, " ") { DEFAULT_KAFKA_PORT }

        mapper.producer ?: kotlin.run { mapper.producer = KafkaConfigMapper.ProducerConfigMapper() }
        mapper.producer!!.asyncSend ?: kotlin.run { mapper.producer!!.asyncSend = false }
        mapper.producer!!.acks ?: kotlin.run { mapper.producer!!.acks = "all" }
        mapper.producer!!.batchSize ?: kotlin.run { mapper.producer!!.batchSize = 16384 }
        mapper.producer!!.requestTimeout ?: kotlin.run { mapper.producer!!.requestTimeout = 30 * 1000 }
        mapper.producer!!.lingerMs ?: kotlin.run { mapper.producer!!.lingerMs = 0 }
        mapper.producer!!.maxRequestSize ?: kotlin.run { mapper.producer!!.maxRequestSize = 1024 * 1024 }

        mapper.consumer ?: kotlin.run { mapper.consumer = KafkaConfigMapper.ConsumerConfigMapper() }
        mapper.consumer!!.autoCommit ?: kotlin.run { mapper.consumer!!.autoCommit = false }
        mapper.consumer!!.autoCommitInterval ?: kotlin.run { mapper.consumer!!.autoCommitInterval = 1000 }
        mapper.consumer!!.maxPollRecords ?: kotlin.run { mapper.consumer!!.maxPollRecords = 500 }
        mapper.consumer!!.maxPollInterval ?: kotlin.run { mapper.consumer!!.maxPollInterval = 300000 }
        mapper.consumer!!.autoOffset ?: kotlin.run { mapper.consumer!!.autoOffset = "latest" }
        mapper.consumer!!.requestTimeout ?: kotlin.run { mapper.consumer!!.requestTimeout = 305000 }
        mapper.consumer!!.bufferSize ?: kotlin.run { mapper.consumer!!.bufferSize = 65536 }
    }
}
