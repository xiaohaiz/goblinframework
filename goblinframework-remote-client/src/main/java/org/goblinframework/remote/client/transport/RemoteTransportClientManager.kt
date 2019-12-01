package org.goblinframework.remote.client.transport

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject

@Singleton
@ThreadSafe
@GoblinManagedBean("RemoteClient")
@GoblinManagedLogger("goblin.remote.client.transport")
class RemoteTransportClientManager private constructor()
  : GoblinManagedObject(), RemoteTransportClientManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteTransportClientManager()
  }

  fun openConnection(node: String): RemoteTransportClient {
    TODO()
  }
}