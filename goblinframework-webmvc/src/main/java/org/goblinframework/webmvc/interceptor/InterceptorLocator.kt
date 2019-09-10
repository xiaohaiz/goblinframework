package org.goblinframework.webmvc.interceptor

import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.core.util.ObjectUtils
import org.goblinframework.webmvc.setting.InterceptorSetting
import org.springframework.beans.factory.BeanFactoryUtils

class InterceptorLocator(setting: InterceptorSetting) {

  private val includeDefaultInterceptors = setting.includeDefaultInterceptors()
  private val staticInterceptors = mutableListOf<Interceptor>()
  private val managedInterceptors = mutableListOf<ContainerManagedBean>()

  init {
    staticInterceptors.addAll(setting.interceptors())
    setting.applicationContext()?.run {
      BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this, Interceptor::class.java, true, false)
          .map { ContainerManagedBean(it, this) }
          .forEach { managedInterceptors.add(it) }
    }
  }

  fun locate(lookupPath: String): InterceptorList {
    val list = mutableListOf<Interceptor>()
    list.addAll(staticInterceptors)
    managedInterceptors.map { it.getBean() as Interceptor }.forEach { list.add(it) }
    val interceptors = InterceptorList()
    list.sortedWith(ObjectUtils.ORDERED_COMPARATOR).forEach { interceptors.add(it) }
    if (includeDefaultInterceptors) {
      interceptors.add(0, NoCacheInterceptor.INSTANCE)
    }
    interceptors.removeAll { !it.matches(lookupPath) }
    return interceptors
  }
}