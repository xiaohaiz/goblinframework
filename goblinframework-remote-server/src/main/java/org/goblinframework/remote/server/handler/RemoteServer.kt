package org.goblinframework.remote.server.handler

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.common.ThreadSafe
import org.goblinframework.api.common.Lifecycle
import org.goblinframework.api.service.GoblinManagedObject
import java.util.concurrent.atomic.AtomicReference

@Singleton
@ThreadSafe
class RemoteServer private constructor()
  : GoblinManagedObject(), Lifecycle, RemoteServerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteServer()
  }

  private val server = AtomicReference<RemoteServerImpl>()

  @Synchronized
  override fun start() {
    if (server.get() == null) {
      server.set(RemoteServerImpl())
    }
  }

  override fun stop() {
    server.getAndSet(null)?.close()
  }

  override fun isRunning(): Boolean {
    return server.get() != null
  }

  override fun disposeBean() {
    stop()
  }
}