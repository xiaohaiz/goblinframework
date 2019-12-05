package org.goblinframework.remote.client.module.container

import org.goblinframework.api.remote.ImportService
import org.goblinframework.core.container.SpringContainerBeanPostProcessor
import org.goblinframework.core.util.GoblinField
import org.goblinframework.core.util.ReflectionUtils
import org.goblinframework.remote.client.invocation.invoker.java.RemoteJavaClientFactory

class InjectRemoteService private constructor() : SpringContainerBeanPostProcessor {

  companion object {
    @JvmField val INSTANCE = InjectRemoteService()
  }

  override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
    ReflectionUtils.allFieldsIncludingAncestors(bean.javaClass, false, false).forEach {
      val annotation = it.getAnnotation(ImportService::class.java)
      if (annotation != null && annotation.enable) {
        val client: Any = RemoteJavaClientFactory.createJavaClient(it)
        GoblinField(it).set(bean, client)
      }
    }
    return bean
  }

}