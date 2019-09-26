package org.goblinframework.remote.client.service

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.container.SpringContainerBeanPostProcessor
import org.goblinframework.api.remote.ImportService
import org.goblinframework.core.util.GoblinField
import org.goblinframework.core.util.ReflectionUtils

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
    val beanType = bean.javaClass
    val fields = ReflectionUtils.allFieldsIncludingAncestors(beanType, false, false)
        .map { GoblinField(it) }.toList()
    val candidates = mutableListOf<ImportServiceField>()
    for (field in fields) {
      val annotation = field.findAnnotationSetterFirst(ImportService::class.java) ?: continue
      if (annotation.enable) {
        candidates.add(ImportServiceField(bean, field, annotation))
      }
    }
    candidates.forEach { it.inject() }
    return bean
  }
}