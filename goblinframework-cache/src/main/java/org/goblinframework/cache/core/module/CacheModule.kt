package org.goblinframework.cache.core.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.system.*
import org.goblinframework.cache.core.cache.CacheBuilderManager
import org.goblinframework.cache.core.cache.GoblinCacheBuilderManager
import org.goblinframework.cache.core.module.test.FlushInJvmCacheBeforeTestMethod

@Install
class CacheModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.CACHE
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.registerTestExecutionListener(FlushInJvmCacheBeforeTestMethod.INSTANCE)
    //ctx.registerGoblinCacheBuilder(GoblinCacheSystem.JVM, InJvmCacheBuilder.INSTANCE)
    //ctx.registerGoblinCacheBuilder(GoblinCacheSystem.NOP, NoOpCacheBuilder.INSTANCE)
    //ctx.registerInstructionTranslator(VMC::class.java, VMCTranslator.INSTANCE)
    ctx.createSubModules()
        .module(GoblinSubModule.CACHE_COUCHBASE)
        .module(GoblinSubModule.CACHE_REDIS)
        .install(ctx)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    ctx.createSubModules()
        .module(GoblinSubModule.CACHE_COUCHBASE)
        .module(GoblinSubModule.CACHE_REDIS)
        .initialize(ctx)
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    GoblinCacheBuilderManager.INSTANCE.dispose()
    ctx.createSubModules()
        .module(GoblinSubModule.CACHE_COUCHBASE)
        .module(GoblinSubModule.CACHE_REDIS)
        .finalize(ctx)
    CacheBuilderManager.INSTANCE.dispose()
  }
}