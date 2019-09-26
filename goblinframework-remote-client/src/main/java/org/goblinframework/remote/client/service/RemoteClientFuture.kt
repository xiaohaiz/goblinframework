package org.goblinframework.remote.client.service

import org.goblinframework.api.core.GoblinFutureImpl
import java.util.concurrent.atomic.AtomicInteger

class RemoteClientFuture : GoblinFutureImpl<RemoteClient?>() {

  internal val neonatalCount = AtomicInteger()

  internal fun finishConnection(client: RemoteClient?) {
    client?.run {
      complete(this)
      return
    }
    if (neonatalCount.decrementAndGet() == 0) {
      complete(null)
    }
  }
}