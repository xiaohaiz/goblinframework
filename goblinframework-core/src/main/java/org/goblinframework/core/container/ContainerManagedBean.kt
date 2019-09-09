package org.goblinframework.core.container

import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.NoSuchBeanDefinitionException

class ContainerManagedBean(val beanName: String, val beanFactory: BeanFactory) {

  val singleton: Boolean = try {
    beanFactory.isSingleton(beanName)
  } catch (ex: NoSuchBeanDefinitionException) {
    false
  }
  private val singletonBean: Any? = if (singleton) {
    beanFactory.getBean(beanName)
  } else {
    null
  }

  fun getBean(): Any? {
    return singletonBean ?: try {
      beanFactory.getBean(beanName)
    } catch (ex: NoSuchBeanDefinitionException) {
      null
    }
  }
}