package org.goblinframework.cache.core.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.cache.core.cache.GoblinCacheBuilderManager
import org.goblinframework.core.bootstrap.GoblinModule
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.core.bootstrap.GoblinModuleInitializeContext
import org.goblinframework.core.bootstrap.GoblinModuleInstallContext

@Install
class CacheModule : GoblinModule {

  override fun name(): String {
    return "CACHE"
  }

  override fun install(ctx: GoblinModuleInstallContext) {
    ctx.createChildModuleManager()
        .module("CACHE:COUCHBASE")
        .module("CACHE:REDIS")
        .initialize(ctx)
  }

  override fun initialize(ctx: GoblinModuleInitializeContext) {
    ctx.createChildModuleManager()
        .module("CACHE:COUCHBASE")
        .module("CACHE:REDIS")
        .bootstrap(ctx)
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    GoblinCacheBuilderManager.INSTANCE.dispose()
    ctx.createChildModuleManager()
        .module("CACHE:COUCHBASE")
        .module("CACHE:REDIS")
        .finalize(ctx)
  }
}