package org.goblinframework.management.server

import org.goblinframework.webmvc.view.FreemarkerViewResolver

class ManagementViewResolver private constructor() : FreemarkerViewResolver() {

  companion object {
    @JvmField val INSTANCE = ManagementViewResolver()
  }

  init {
    setName("ManagementViewResolver")
    setSuffix(".ftl")
    setAutoInclude("/common.ftl")
    setTemplateLoaderPaths("/management/ftl")
    setPreferFileSystemAccess(false)
    afterPropertiesSet()
  }
}