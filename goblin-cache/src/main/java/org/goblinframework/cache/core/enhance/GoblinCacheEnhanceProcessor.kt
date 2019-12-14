package org.goblinframework.cache.core.enhance

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.cache.bean.CacheBeanManager
import org.goblinframework.cache.exception.GoblinCacheException
import org.goblinframework.core.container.SpringContainerBeanPostProcessor
import org.goblinframework.core.util.ClassUtils
import org.springframework.aop.framework.ProxyFactory
import java.lang.reflect.Modifier

@Singleton
class GoblinCacheEnhanceProcessor private constructor()
  : SpringContainerBeanPostProcessor {

  companion object {
    @JvmField val INSTANCE = GoblinCacheEnhanceProcessor()
  }

  override fun postProcessAfterInitialization(bean: Any?, beanName: String?): Any? {
    return bean?.run { tryGoblinCacheEnhance(this) }
  }

  private fun tryGoblinCacheEnhance(bean: Any): Any {
    val capsule = CacheBeanManager.getGoblinCacheBean(bean.javaClass)
    if (capsule.isEmpty) {
      return bean
    }
    if (Modifier.isFinal(bean.javaClass.modifiers)) {
      throw GoblinCacheException("Final class not supported: ${bean.javaClass.name}")
    }

    val interceptor = GoblinCacheInterceptor(bean, capsule)
    val proxyFactory = ProxyFactory()
    proxyFactory.isProxyTargetClass = true
    proxyFactory.setTarget(bean)
    proxyFactory.addAdvice(interceptor)
    return proxyFactory.getProxy(ClassUtils.getDefaultClassLoader())
  }
}
