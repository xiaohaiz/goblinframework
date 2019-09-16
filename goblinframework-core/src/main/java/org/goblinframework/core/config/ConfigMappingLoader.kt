package org.goblinframework.core.config

import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject

@GoblinManagedBean("CORE")
class ConfigMappingLoader internal constructor() : GoblinManagedObject(), ConfigMappingLoaderMXBean {

  internal fun close() {
    unregisterIfNecessary()
  }
}