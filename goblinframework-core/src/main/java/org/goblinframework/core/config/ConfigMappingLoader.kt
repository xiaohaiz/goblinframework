package org.goblinframework.core.config

import org.goblinframework.core.mapper.JsonMapper
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import java.util.concurrent.atomic.AtomicReference

@GoblinManagedBean("CORE")
class ConfigMappingLoader internal constructor() : GoblinManagedObject(), ConfigMappingLoaderMXBean {

  private val mapping = AtomicReference<ConfigMapping>()

  init {
    mapping.set(ConfigMapping())
  }

  internal fun initialize(scanner: ConfigLocationScanner) {
    if (scanner.getConfigPathUrl() == null) {
      return
    }
    val resource = scanner.scan("goblin.json").firstOrNull() ?: return
    val mapping = resource.inputStream.use {
      JsonMapper.asObject(it, ConfigMapping::class.java)
    }
    this.mapping.set(mapping)
  }

  internal fun close() {
    unregisterIfNecessary()
  }
}