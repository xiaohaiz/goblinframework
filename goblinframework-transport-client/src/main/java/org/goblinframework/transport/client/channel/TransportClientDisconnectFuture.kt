package org.goblinframework.transport.client.channel

import org.goblinframework.core.concurrent.GoblinFutureImpl

class TransportClientDisconnectFuture(clientManager: TransportClientManager) : GoblinFutureImpl<TransportClient>() {

  init {
    clientManager.registerDisconnectFuture(this)
    addListener {
      clientManager.unregisterDisconnectFuture(this)
    }
  }
}