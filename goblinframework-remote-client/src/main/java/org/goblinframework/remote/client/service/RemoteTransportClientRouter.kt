package org.goblinframework.remote.client.service

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.remote.client.transport.RemoteTransportClient

@GoblinManagedBean("RemoteClient")
@GoblinManagedLogger("goblin.remote.client.service")
class RemoteTransportClientRouter internal constructor()
  : GoblinManagedObject(), RemoteTransportClientRouterMXBean {

  fun addConnection(connection: RemoteTransportClient) {

  }

  fun removeConnection(connection: RemoteTransportClient) {

  }
}