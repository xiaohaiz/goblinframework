package org.goblinframework.core.config

import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import java.util.concurrent.atomic.AtomicBoolean

@GoblinManagedBean("CORE")
class ConfigMappingLoader internal constructor() : GoblinManagedObject(), ConfigMappingLoaderMXBean {

  private val initialized = AtomicBoolean()

  internal fun initialize(scanner: ConfigLocationScanner) {
    if (scanner.getConfigPathUrl() == null) {
      return
    }
    initialized.set(true)
  }

  internal fun close() {
    unregisterIfNecessary()
  }
}