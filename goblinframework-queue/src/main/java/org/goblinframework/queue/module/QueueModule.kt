package org.goblinframework.queue.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.*

@Install
class QueueModule : IModule {
    override fun id(): GoblinModule {
        return GoblinModule.QUEUE
    }

    override fun install(ctx: ModuleInstallContext) {
        ctx.createSubModules()
                .module(GoblinSubModule.QUEUE_KAFKA)
                .module(GoblinSubModule.QUEUE_RABBITMQ)
                .install(ctx)
    }

    override fun initialize(ctx: ModuleInitializeContext) {
        ctx.createSubModules()
                .module(GoblinSubModule.QUEUE_KAFKA)
                .module(GoblinSubModule.QUEUE_RABBITMQ)
                .initialize(ctx)
    }

    override fun finalize(ctx: ModuleFinalizeContext) {
        ctx.createSubModules()
                .module(GoblinSubModule.QUEUE_KAFKA)
                .module(GoblinSubModule.QUEUE_RABBITMQ)
                .finalize(ctx)
    }
}