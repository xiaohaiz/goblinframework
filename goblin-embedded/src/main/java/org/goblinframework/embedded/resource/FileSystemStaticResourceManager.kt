package org.goblinframework.embedded.resource

import org.goblinframework.core.util.StringUtils
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource

class FileSystemStaticResourceManager(buffer: StaticResourceBuffer, targetPrefix: String, baseDir: String) : AbstractStaticResourceManager(buffer, targetPrefix) {

  private val baseDir: String

  init {
    var c = StringUtils.defaultString(baseDir)
    if (!StringUtils.endsWith(c, "/")) {
      c = "$c/"
    }
    this.baseDir = c
  }

  override fun loadResource(lookupPath: String): Resource? {
    val t = StringUtils.substringAfter(lookupPath, getLookupPathPrefix())
    val location = baseDir + t
    return FileSystemResource(location)
  }
}
