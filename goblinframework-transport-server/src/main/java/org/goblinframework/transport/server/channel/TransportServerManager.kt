package org.goblinframework.transport.server.channel

import org.goblinframework.api.core.GoblinManagedBean
import org.goblinframework.api.core.GoblinManagedObject
import org.goblinframework.api.core.Singleton
import org.goblinframework.api.core.ThreadSafe
import org.goblinframework.core.exception.GoblinDuplicateException
import org.goblinframework.transport.server.setting.TransportServerSetting
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
@ThreadSafe
@GoblinManagedBean(type = "transport.server")
class TransportServerManager private constructor() : GoblinManagedObject(), TransportServerManagerMXBean {

  companion object {
    @JvmField val INSTANCE = TransportServerManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val buffer = mutableMapOf<String, TransportServer>()

  /**
   * Get already created transport server instance, return null
   * in case of specified server name not found.
   */
  fun getTransportServer(name: String): TransportServer? {
    return lock.read { buffer[name] }
  }

  /**
   * Create transport server of specified setting, never return
   * null. Raise [GoblinDuplicateException] in case of server name
   * already created.
   */
  fun createTransportServer(setting: TransportServerSetting): TransportServer {
    val name = setting.name()
    return lock.write {
      buffer[name]?.run {
        throw GoblinDuplicateException("Transport server [$name] already created")
      }
      val server = TransportServer(setting)
      buffer[name] = server
      server
    }
  }

  /**
   * Close transport server of specified name. Do nothing in case
   * of server name not found.
   */
  fun closeTransportServer(name: String) {
    lock.write { buffer.remove(name) }?.dispose()
  }

  override fun disposeBean() {
    lock.write {
      buffer.values.forEach { it.dispose() }
      buffer.clear()
    }
  }

  override fun getTransportServerList(): Array<TransportServerMXBean> {
    return lock.read { buffer.values.toTypedArray() }
  }
}