package org.goblinframework.cache.core.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.cache.CacheSystem
import org.goblinframework.api.system.*
import org.goblinframework.cache.core.cache.CacheBuilderManager
import org.goblinframework.cache.core.module.test.FlushInJvmCacheBeforeTestMethod
import org.goblinframework.cache.core.provider.InJvmCacheBuilder
import org.goblinframework.cache.core.provider.NoOpCacheBuilder

@Install
class CacheModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.CACHE
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.registerTestExecutionListener(FlushInJvmCacheBeforeTestMethod.INSTANCE)
    CacheBuilderManager.INSTANCE.register(CacheSystem.NOP, NoOpCacheBuilder.INSTANCE)
    CacheBuilderManager.INSTANCE.register(CacheSystem.JVM, InJvmCacheBuilder.INSTANCE)
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
    CacheBuilderManager.INSTANCE.dispose()
    ctx.createSubModules()
        .module(GoblinSubModule.CACHE_COUCHBASE)
        .module(GoblinSubModule.CACHE_REDIS)
        .finalize(ctx)
  }
}