package org.goblinframework.core.config

import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.util.ClassUtils
import org.goblinframework.core.util.IOUtils
import org.springframework.core.io.ByteArrayResource
import java.util.concurrent.atomic.AtomicReference

@GoblinManagedBean("CORE")
class MappingLocationScanner
internal constructor(configLocationScanner: ConfigLocationScanner)
  : GoblinManagedObject(), MappingLocationScannerMXBean {

  companion object {
    private val SCAN_SEQUENCE = listOf(
        "config/goblin-test.json",
        "META-INF/goblin/goblin-test.json",
        "config/goblin.json",
        "META-INF/goblin/goblin.json")
  }

  private val mappingLocation = AtomicReference<MappingLocation>()

  init {
    val classLoader = ClassUtils.getDefaultClassLoader()
    for (path in SCAN_SEQUENCE) {
      val url = classLoader.getResource(path)
      if (url != null) {
        val resource = url.openStream().use {
          val bs = IOUtils.toByteArray(it)
          ByteArrayResource(bs)
        }
        mappingLocation.set(MappingLocation(url, resource))
        break
      }
    }
    if (mappingLocation.get() == null) {
      val resource = configLocationScanner.scan("goblin.json").firstOrNull()
      resource?.run {
        mappingLocation.set(MappingLocation(this.url, this))
      }
    }
  }

  fun getMappingLocation(): MappingLocation? {
    return mappingLocation.get()
  }

  internal fun close() {
    unregisterIfNecessary()
  }
}