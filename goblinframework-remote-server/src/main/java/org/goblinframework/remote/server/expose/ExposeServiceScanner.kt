package org.goblinframework.remote.server.expose

import org.goblinframework.remote.server.service.RemoteServiceManager
import org.goblinframework.remote.server.service.StaticRemoteService

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
}