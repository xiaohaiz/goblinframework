package org.goblinframework.remote.server.service

import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.remote.server.expose.ExposeServiceId

@GoblinManagedBean("REMOTE.SERVER")
class StaticRemoteService(id: ExposeServiceId,
                          private val bean: Any)
  : RemoteService(id) {

  override fun getMode(): String {
    return "STATIC"
  }

  override fun type(): Class<*> {
    return bean.javaClass
  }

  override fun bean(): Any {
    return bean
  }
}