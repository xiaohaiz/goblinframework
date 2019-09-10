package org.goblinframework.webmvc.view

import org.goblinframework.core.util.ClassUtils
import org.springframework.core.io.ClassPathResource
import java.util.*

object FreemarkerSetting {

  private val CONFIG_PATHS = arrayOf(
      "META-INF/goblin/default.freemarker.properties",
      "META-INF/goblin/freemarker.properties")

  val setting: Properties by lazy {
    val props = Properties()
    for (path in CONFIG_PATHS) {
      val resource = ClassPathResource(path, ClassUtils.getDefaultClassLoader())
      if (!resource.exists() || !resource.isReadable) {
        continue
      }
      resource.inputStream.use { props.load(it) }
    }
    props
  }
}