package org.goblinframework.cache.core.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.cache.core.cache.CacheBuilderManager
import org.goblinframework.cache.core.cache.CacheBuilderManager2
import org.goblinframework.cache.core.enhance.GoblinCacheEnhanceProcessor
import org.goblinframework.cache.core.module.management.CacheManagement
import org.goblinframework.cache.core.module.test.FlushCacheBeforeTestMethod
import org.goblinframework.core.system.*

@Install
class CacheModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.CACHE
  }

  override fun managementEntrance(): String? {
    return "/goblin/cache/index.do"
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.registerTestExecutionListener(FlushCacheBeforeTestMethod.INSTANCE)
    ctx.registerContainerBeanPostProcessor(GoblinCacheEnhanceProcessor.INSTANCE)
    ctx.registerManagementController(CacheManagement.INSTANCE)
    ctx.createSubModules()
        .module(GoblinSubModule.CACHE_COUCHBASE)
        .module(GoblinSubModule.CACHE_REDIS)
        .install(ctx)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    CacheBuilderManager2.INSTANCE.initialize()
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
    CacheBuilderManager2.INSTANCE.dispose()
  }

}