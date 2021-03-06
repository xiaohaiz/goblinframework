package org.goblinframework.queue.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.*
import org.goblinframework.queue.consumer.builder.QueueConsumerBuilderManager
import org.goblinframework.queue.consumer.processor.QueueListenerProcessor
import org.goblinframework.queue.module.management.QueueManagement
import org.goblinframework.queue.producer.builder.QueueProducerBuilderManager
import org.goblinframework.queue.producer.processor.QueueProducerProcessor

@Install
class QueueModule : IModule {
    override fun id(): GoblinModule {
        return GoblinModule.QUEUE
    }

    override fun managementEntrance(): String? {
        return "/goblin/queue/index.do"
    }

    override fun install(ctx: ModuleInstallContext) {
        ctx.createSubModules()
                .module(GoblinSubModule.QUEUE_KAFKA)
                .module(GoblinSubModule.QUEUE_RABBITMQ)
                .install(ctx)
        ctx.registerManagementController(QueueManagement.INSTANCE)
        ctx.registerContainerBeanPostProcessor(QueueProducerProcessor.INSTANCE)
        ctx.subscribeEventListener(QueueListenerProcessor.INSTANCE)
    }

    override fun initialize(ctx: ModuleInitializeContext) {
        QueueChannelManager.INSTANCE.initialize()
        ctx.createSubModules()
                .module(GoblinSubModule.QUEUE_KAFKA)
                .module(GoblinSubModule.QUEUE_RABBITMQ)
                .initialize(ctx)
    }

    override fun finalize(ctx: ModuleFinalizeContext) {
        QueueConsumerBuilderManager.INSTANCE.dispose()
        QueueProducerBuilderManager.INSTANCE.dispose()
        QueueChannelManager.INSTANCE.shutdown()
        ctx.createSubModules()
                .module(GoblinSubModule.QUEUE_KAFKA)
                .module(GoblinSubModule.QUEUE_RABBITMQ)
                .finalize(ctx)
    }
}