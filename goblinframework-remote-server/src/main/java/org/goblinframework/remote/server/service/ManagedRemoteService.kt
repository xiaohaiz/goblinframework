package org.goblinframework.remote.server.service

import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.remote.server.expose.ExposeServiceId

@GoblinManagedBean("REMOTE.SERVER")
class ManagedRemoteService(id: ExposeServiceId,
                           private val bean: ContainerManagedBean)
  : RemoteService(id) {

  override fun getMode(): String {
    return "MANAGED"
  }
}