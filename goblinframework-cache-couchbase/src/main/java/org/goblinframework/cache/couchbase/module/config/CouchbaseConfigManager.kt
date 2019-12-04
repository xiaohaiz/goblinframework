package org.goblinframework.cache.couchbase.module.config

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@Singleton
@GoblinManagedBean("CacheCouchbase")
class CouchbaseConfigManager private constructor()
  : GoblinManagedObject(), CouchbaseConfigManagerMXBean {

  companion object {
    @JvmField val INSTANCE = CouchbaseConfigManager()
  }

  private val configParser = CouchbaseConfigParser()

  override fun initializeBean() {
    configParser.initialize()
  }

  fun getCouchbaseConfig(name: String): CouchbaseConfig? {
    return configParser.getFromBuffer(name)
  }

  override fun disposeBean() {
    configParser.dispose()
  }
}