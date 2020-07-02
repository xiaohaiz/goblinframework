package org.goblinframework.queue.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.*
import org.goblinframework.queue.module.config.KafkaConfigManager

@Install
class QueueKafkaModule : ISubModule {
    override fun id(): GoblinSubModule {
        return GoblinSubModule.QUEUE_KAFKA
    }

    override fun initialize(ctx: ModuleInitializeContext) {
        KafkaConfigManager.INSTANCE.initialize()
    }

    override fun install(ctx: ModuleInstallContext) {
    }

    override fun finalize(ctx: ModuleFinalizeContext) {
        KafkaConfigManager.INSTANCE.dispose()
    }
}