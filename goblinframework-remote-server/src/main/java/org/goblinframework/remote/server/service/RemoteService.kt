package org.goblinframework.remote.server.service

import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.remote.server.expose.ExposeServiceId

@GoblinManagedBean("REMOTE.SERVER")
abstract class RemoteService(private val id: ExposeServiceId)
  : GoblinManagedObject(), RemoteServiceMXBean {

  fun id(): ExposeServiceId {
    return id
  }

  abstract fun type(): Class<*>

  abstract fun bean(): Any
}