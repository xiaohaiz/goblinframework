package org.goblinframework.embedded.core.resource

import org.goblinframework.core.util.ClassUtils
import org.goblinframework.core.util.StringUtils
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource

class ClassPathStaticResourceManager(buffer: StaticResourceBuffer, targetPrefix: String, classPathPrefix: String) : AbstractStaticResourceManager(buffer, targetPrefix) {

  private val classPathPrefix: String

  init {
    var c = StringUtils.defaultString(classPathPrefix)
    if (!StringUtils.endsWith(c, "/")) {
      c = "$c/"
    }
    this.classPathPrefix = c
  }

  override fun loadResource(lookupPath: String): Resource? {
    val t = StringUtils.substringAfter(lookupPath, getLookupPathPrefix())
    val location = classPathPrefix + t
    return ClassPathResource(location, ClassUtils.getDefaultClassLoader())
  }
}
