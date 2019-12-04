package org.goblinframework.cache.couchbase.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.GoblinSubModule
import org.goblinframework.core.system.ISubModule

@Install
class CacheCouchbaseModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.CACHE_COUCHBASE
  }
}