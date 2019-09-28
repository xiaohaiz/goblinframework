package org.goblinframework.transport.server.channel

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.api.core.Lifecycle
import org.goblinframework.core.util.StopWatch
import org.goblinframework.transport.server.setting.TransportServerSetting
import java.util.concurrent.atomic.AtomicReference

@GoblinManagedBean(type = "transport.server")
class TransportServer
internal constructor(private val setting: TransportServerSetting)
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

  override fun disposeBean() {
    watch.stop()
    stop()
  }

  override fun getUpTime(): String {
    return watch.toString()
  }

  override fun getName(): String {
    return setting.name()
  }

  override fun getRunning(): Boolean {
    return isRunning
  }

  override fun getHost(): String? {
    return server.get()?.host
  }

  override fun getPort(): Int {
    return server.get()?.port ?: -1
  }
}