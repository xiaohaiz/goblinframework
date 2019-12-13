package org.goblinframework.cache.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.cache.core.cache.CacheBuilderManager
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
    CacheBuilderManager.INSTANCE.initialize()
    ctx.createSubModules()
        .module(GoblinSubModule.CACHE_COUCHBASE)
        .module(GoblinSubModule.CACHE_REDIS)
        .initialize(ctx)
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    ctx.createSubModules()
        .module(GoblinSubModule.CACHE_COUCHBASE)
        .module(GoblinSubModule.CACHE_REDIS)
        .finalize(ctx)
    CacheBuilderManager.INSTANCE.dispose()
  }

}