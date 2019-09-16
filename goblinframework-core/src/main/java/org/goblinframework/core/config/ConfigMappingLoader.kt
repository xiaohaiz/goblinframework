package org.goblinframework.core.config

import org.goblinframework.core.mapper.JsonMapper
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.util.ClassUtils
import org.springframework.core.io.ClassPathResource
import java.util.concurrent.atomic.AtomicReference

@GoblinManagedBean("CORE")
class ConfigMappingLoader
internal constructor(private val configLocationScanner: ConfigLocationScanner)
  : GoblinManagedObject(), ConfigMappingLoaderMXBean {

  val mapping = AtomicReference<ConfigMapping>(ConfigMapping())

  internal fun initialize(configMappingFile: String) {
    val resource = if (configMappingFile.startsWith("classpath:")) {
      val path = configMappingFile.substringAfter("classpath:")
      val cpr = ClassPathResource(path, ClassUtils.getDefaultClassLoader())
      if (cpr.exists() && cpr.isReadable) cpr else null
    } else {
      configLocationScanner.scan(configMappingFile).firstOrNull()
    } ?: return
    val mapping = resource.inputStream.use {
      JsonMapper.asObject(it, ConfigMapping::class.java)
    }
    this.mapping.set(mapping)
  }

  internal fun close() {
    unregisterIfNecessary()
  }
}