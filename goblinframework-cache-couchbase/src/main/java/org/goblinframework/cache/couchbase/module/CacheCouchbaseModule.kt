package org.goblinframework.cache.couchbase.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.cache.couchbase.client.CouchbaseEnvironmentManager
import org.goblinframework.core.system.GoblinSubModule
import org.goblinframework.core.system.ISubModule
import org.goblinframework.core.system.ModuleFinalizeContext
import org.goblinframework.core.system.ModuleInitializeContext

@Install
class CacheCouchbaseModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.CACHE_COUCHBASE
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    CouchbaseEnvironmentManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    CouchbaseEnvironmentManager.INSTANCE.dispose()
  }
}