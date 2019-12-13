package org.goblinframework.cache.bean

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@GoblinManagedBean("Cache")
class GoblinCacheBean internal constructor() : GoblinManagedObject(), GoblinCacheBeanMXBean {
}