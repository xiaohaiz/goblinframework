package org.goblinframework.remote.server.expose

import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.remote.server.service.ManagedRemoteService
import org.goblinframework.remote.server.service.RemoteServiceManager
import org.goblinframework.remote.server.service.StaticRemoteService
import org.springframework.context.ApplicationContext

object ExposeServiceScanner {

  fun expose(vararg beans: Any) {
    if (beans.isEmpty()) {
      return
    }
    for (bean in beans) {
      val ids = ExposeServiceIdGenerator.generate(bean.javaClass)
      if (ids.isEmpty()) {
        continue
      }
      val serviceManager = RemoteServiceManager.INSTANCE
      for (id in ids) {
        val service = StaticRemoteService(id, bean)
        serviceManager.register(service)
      }
    }
  }

  fun expose(applicationContext: ApplicationContext) {
    val beanNames = applicationContext.getBeanNamesForType(Any::class.java, true, false)
    for (beanName in beanNames) {
      val type = applicationContext.getType(beanName) ?: continue
      val ids = ExposeServiceIdGenerator.generate(type)
      if (ids.isEmpty()) {
        continue
      }
      val serviceManager = RemoteServiceManager.INSTANCE
      for (id in ids) {
        val bean = ContainerManagedBean(beanName, applicationContext)
        val service = ManagedRemoteService(id, bean)
        serviceManager.register(service)
      }
    }
  }
}