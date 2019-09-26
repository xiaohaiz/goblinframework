package org.goblinframework.remote.client.connection

import org.goblinframework.api.core.GoblinManagedBean
import org.goblinframework.api.core.Singleton
import org.goblinframework.core.util.HttpUtils
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
@GoblinManagedBean(type = "RemoteClient")
class RemoteConnectionManager private constructor() {

  companion object {
    @JvmField val INSTANCE = RemoteConnectionManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val buffer = mutableMapOf<RemoteConnectionId, RemoteConnection>()

  fun openConnection(node: String): RemoteConnection {
    val map = HttpUtils.parseQueryString(node)
    val serverId = map["id"]!!
    val serverHost = map["host"]!!
    val serverPort = map["port"]!!.toInt()
    val connectionId = RemoteConnectionId(serverId, serverHost, serverPort)
    lock.read {
      buffer[connectionId]?.run {
        this.retain()
        return this
      }
    }
    lock.write {
      buffer[connectionId]?.run {
        this.retain()
        return this
      }
      val connection = RemoteConnection(connectionId)
      connection.retain()
      buffer[connectionId] = connection
      return connection
    }
  }

  fun closeConnection(connectionId: RemoteConnectionId) {
    lock.write {
      val connection = buffer[connectionId] ?: return
      if (connection.release()) {
        buffer.remove(connectionId)
        connection.dispose()
      }
    }
  }
}