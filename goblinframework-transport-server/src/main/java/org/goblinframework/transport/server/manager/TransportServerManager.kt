package org.goblinframework.transport.server.manager

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.api.common.DuplicateException
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.transport.server.setting.ServerSetting
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
@ThreadSafe
@GoblinManagedBean("TRANSPORT.SERVER")
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
   * null. Raise [DuplicateException] in case of server name
   * already created.
   */
  fun createTransportServer(setting: ServerSetting): TransportServer {
    val name = setting.name()
    return lock.write {
      buffer[name]?.run {
        throw DuplicateException("Transport server [$name] already created")
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
    lock.write { buffer.remove(name) }?.close()
  }

  fun close() {
    unregisterIfNecessary()
    lock.write {
      buffer.values.forEach { it.close() }
      buffer.clear()
    }
  }

  override fun getTransportServerList(): Array<TransportServerMXBean> {
    return lock.read { buffer.values.toTypedArray() }
  }
}