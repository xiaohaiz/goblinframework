package org.goblinframework.cache.couchbase.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.cache.core.module.CacheModule
import org.goblinframework.cache.couchbase.client.CouchbaseClientManager
import org.goblinframework.cache.couchbase.client.CouchbaseClusterManager
import org.goblinframework.cache.couchbase.client.CouchbaseEnvironmentManager
import org.goblinframework.cache.couchbase.module.config.CouchbaseConfigManager
import org.goblinframework.cache.couchbase.provider.CouchbaseCacheBuilder
import org.goblinframework.core.system.*

@Install
class CacheCouchbaseModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.CACHE_COUCHBASE
  }

  override fun install(ctx: ModuleInstallContext) {
    val parent = ctx.getExtension(CacheModule::class.java)
    parent?.registerCacheBuilder(CouchbaseCacheBuilder.INSTANCE)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    CouchbaseConfigManager.INSTANCE.initialize()
    CouchbaseEnvironmentManager.INSTANCE.initialize()
    CouchbaseClusterManager.INSTANCE.initialize()
    CouchbaseClientManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    CouchbaseClientManager.INSTANCE.dispose()
    CouchbaseClusterManager.INSTANCE.dispose()
    CouchbaseEnvironmentManager.INSTANCE.dispose()
    CouchbaseConfigManager.INSTANCE.initialize()
  }
}