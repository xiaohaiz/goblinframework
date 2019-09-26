package org.goblinframework.remote.client.service

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.container.SpringContainerBeanPostProcessor

@Singleton
class ImportServiceProcessor private constructor() : SpringContainerBeanPostProcessor {

  companion object {
    @JvmField val INSTANCE = ImportServiceProcessor()
  }

  override fun getOrder(): Int {
    return -1
  }

  override fun postProcessBeforeInitialization(bean: Any?, beanName: String?): Any? {
    return bean?.run { tryProcessImportService(this) }
  }

  private fun tryProcessImportService(bean: Any): Any {
    return bean
  }
}