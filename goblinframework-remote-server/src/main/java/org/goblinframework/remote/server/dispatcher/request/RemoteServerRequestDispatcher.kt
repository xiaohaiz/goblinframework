package org.goblinframework.remote.server.dispatcher.request

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject

@Singleton
@GoblinManagedBean("RemoteServer")
@GoblinManagedLogger("goblin.remote.server.request")
class RemoteServerRequestDispatcher private constructor()
  : GoblinManagedObject(), RemoteServerRequestDispatcherMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteServerRequestDispatcher()
    private const val CHANNEL_NAME = "/goblin/remoter/server/response"
  }
}