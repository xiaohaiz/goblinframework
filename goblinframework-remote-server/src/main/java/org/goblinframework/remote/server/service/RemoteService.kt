package org.goblinframework.remote.server.service

import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject

@GoblinManagedBean(type = "remote.server")
abstract class RemoteService(private val id: ExposeServiceId)
  : GoblinManagedObject(), RemoteServiceMXBean {

  fun id(): ExposeServiceId {
    return id
  }

  abstract fun type(): Class<*>

  abstract fun bean(): Any
}