package org.goblinframework.queue.kafka.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.GoblinSubModule
import org.goblinframework.core.system.ISubModule
import org.goblinframework.core.system.ModuleFinalizeContext
import org.goblinframework.core.system.ModuleInstallContext
import org.goblinframework.queue.kafka.module.config.KafkaConfigManager

@Install
class QueueKafkaModule : ISubModule {
    override fun id(): GoblinSubModule {
        return GoblinSubModule.QUEUE_KAFKA
    }

    override fun install(ctx: ModuleInstallContext) {
        ctx.registerConfigParser(KafkaConfigManager.INSTANCE.configParser)
    }

    override fun finalize(ctx: ModuleFinalizeContext) {
        KafkaConfigManager.INSTANCE.dispose()
    }
}