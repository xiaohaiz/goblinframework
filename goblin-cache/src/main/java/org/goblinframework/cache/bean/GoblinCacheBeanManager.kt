package org.goblinframework.cache.bean

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@Singleton
@ThreadSafe
@GoblinManagedBean("Cache")
class GoblinCacheBeanManager private constructor() : GoblinManagedObject(), GoblinCacheBeanManagerMXBean {

  companion object {
    val INSTANCE = GoblinCacheBeanManager()
  }
}