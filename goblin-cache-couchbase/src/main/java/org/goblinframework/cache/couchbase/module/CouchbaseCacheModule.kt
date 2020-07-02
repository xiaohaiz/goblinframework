package org.goblinframework.cache.couchbase.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.cache.couchbase.cache.CouchbaseCacheBuilder
import org.goblinframework.cache.couchbase.client.CouchbaseClientManager
import org.goblinframework.cache.couchbase.client.CouchbaseClusterManager
import org.goblinframework.cache.couchbase.client.CouchbaseEnvironmentManager
import org.goblinframework.cache.couchbase.module.config.CouchbaseConfigManager
import org.goblinframework.core.system.GoblinSubModule
import org.goblinframework.core.system.ISubModule
import org.goblinframework.core.system.ModuleFinalizeContext
import org.goblinframework.core.system.ModuleInitializeContext

@Install
class CouchbaseCacheModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.CACHE_COUCHBASE
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    CouchbaseConfigManager.INSTANCE.initialize()
    CouchbaseEnvironmentManager.INSTANCE.initialize()
    CouchbaseClusterManager.INSTANCE.initialize()
    CouchbaseClientManager.INSTANCE.initialize()
    CouchbaseCacheBuilder.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    CouchbaseCacheBuilder.INSTANCE.dispose()
    CouchbaseClientManager.INSTANCE.dispose()
    CouchbaseClusterManager.INSTANCE.dispose()
    CouchbaseEnvironmentManager.INSTANCE.dispose()
    CouchbaseConfigManager.INSTANCE.initialize()
  }
}