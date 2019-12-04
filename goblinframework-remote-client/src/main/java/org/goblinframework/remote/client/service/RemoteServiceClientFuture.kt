package org.goblinframework.remote.client.service

import org.goblinframework.core.concurrent.GoblinFutureImpl
import org.goblinframework.core.util.GoblinReferenceCount

class RemoteServiceClientFuture : GoblinFutureImpl<RemoteServiceClient?>() {

  private var referenceCount: GoblinReferenceCount? = null

  internal fun initializeReferenceCount(count: Int) {
    referenceCount = GoblinReferenceCount(count)
  }

  internal fun finishConnection(client: RemoteServiceClient?) {
    client?.run {
      complete(this)
      return
    }
    referenceCount?.run {
      if (this.release()) {
        complete(null)
      }
    }
  }
}