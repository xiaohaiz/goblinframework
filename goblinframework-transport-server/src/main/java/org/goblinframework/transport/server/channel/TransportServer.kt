package org.goblinframework.transport.server.channel

import org.goblinframework.api.core.Lifecycle
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.service.GoblinManagedStopWatch
import org.goblinframework.transport.server.setting.TransportServerSetting
import java.util.concurrent.atomic.AtomicReference

@GoblinManagedBean("TransportServer")
@GoblinManagedStopWatch
class TransportServer internal constructor(private val setting: TransportServerSetting)
  : GoblinManagedObject(), Lifecycle, TransportServerMXBean {

  private val serverReference = AtomicReference<TransportServerImpl?>()

  @Synchronized
  override fun start() {
    if (serverReference.get() == null) {
      serverReference.set(TransportServerImpl(setting))
    }
  }

  override fun stop() {
    serverReference.getAndSet(null)?.close()
  }

  override fun isRunning(): Boolean {
    return serverReference.get() != null
  }

  override fun disposeBean() {
    stop()
  }

  override fun getUpTime(): String? {
    return stopWatch?.toString()
  }

  override fun getName(): String {
    return setting.name()
  }

  override fun getRunning(): Boolean {
    return isRunning()
  }

  override fun getHost(): String? {
    return serverReference.get()?.host
  }

  override fun getPort(): Int? {
    return serverReference.get()?.port
  }
}