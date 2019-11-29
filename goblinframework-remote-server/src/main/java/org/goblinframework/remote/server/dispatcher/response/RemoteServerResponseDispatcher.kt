package org.goblinframework.remote.server.dispatcher.response

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@GoblinManagedBean("RemoteServer")
class RemoteServerResponseDispatcher private constructor()
  : GoblinManagedObject(), RemoteServerResponseDispatcherMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteServerResponseDispatcher()
  }
}