package org.goblinframework.transport.server.manager

import org.goblinframework.api.common.Lifecycle
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.util.StopWatch
import org.goblinframework.transport.server.setting.ServerSetting
import java.util.concurrent.atomic.AtomicReference

@GoblinManagedBean("TRANSPORT.SERVER")
class TransportServer
internal constructor(private val setting: ServerSetting)
  : GoblinManagedObject(), Lifecycle, TransportServerMXBean {

  private val watch = StopWatch()
  private val server = AtomicReference<TransportServerImpl>()

  @Synchronized
  override fun start() {
    if (server.get() == null) {
      server.set(TransportServerImpl(setting))
    }
  }

  override fun stop() {
    server.getAndSet(null)?.close()
  }

  override fun isRunning(): Boolean {
    return server.get() != null
  }

  internal fun close() {
    unregisterIfNecessary()
    watch.stop()
    stop()
  }

  override fun getUpTime(): String {
    return watch.toString()
  }
}