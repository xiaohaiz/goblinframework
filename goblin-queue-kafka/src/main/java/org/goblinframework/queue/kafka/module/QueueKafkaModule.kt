package org.goblinframework.queue.kafka.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.*
import org.goblinframework.queue.kafka.client.KafkaQueueConsumerClientManager
import org.goblinframework.queue.kafka.client.KafkaQueueProducerClientManager
import org.goblinframework.queue.kafka.module.config.KafkaConfigManager
import org.goblinframework.queue.kafka.producer.KafkaQueueProducerBuilder
import org.goblinframework.queue.producer.builder.QueueProducerBuilderManager

@Install
class QueueKafkaModule : ISubModule {
    override fun id(): GoblinSubModule {
        return GoblinSubModule.QUEUE_KAFKA
    }

    override fun initialize(ctx: ModuleInitializeContext) {
        KafkaConfigManager.INSTANCE.initialize()
        KafkaQueueProducerClientManager.INSTANCE.initialize()
        KafkaQueueConsumerClientManager.INSTANCE.initialize()
        QueueProducerBuilderManager.INSTANCE.register(KafkaQueueProducerBuilder.INSTANCE)
    }

    override fun install(ctx: ModuleInstallContext) {
    }

    override fun finalize(ctx: ModuleFinalizeContext) {
        KafkaQueueConsumerClientManager.INSTANCE.dispose()
        KafkaQueueProducerClientManager.INSTANCE.dispose()
        KafkaConfigManager.INSTANCE.dispose()
    }
}