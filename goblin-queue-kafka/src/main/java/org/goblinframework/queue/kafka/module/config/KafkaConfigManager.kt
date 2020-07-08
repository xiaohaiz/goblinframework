package org.goblinframework.queue.kafka.module.config

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@Singleton
@GoblinManagedBean("QueueKafka")
class KafkaConfigManager private constructor()
    : GoblinManagedObject(), KafkaConfigManagerMXBean {

    companion object {
        @JvmField val INSTANCE = KafkaConfigManager()
    }

    private val configParser = KafkaConfigParser()

    override fun initializeBean() {
        configParser.initialize()
    }

    fun getKafkaClient(name: String): KafkaConfig? {
        return configParser.getFromBuffer(name)
    }

    override fun disposeBean() {
        configParser.dispose()
    }
}