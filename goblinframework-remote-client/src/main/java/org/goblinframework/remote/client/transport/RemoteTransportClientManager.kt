package org.goblinframework.remote.client.transport

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.util.HttpUtils
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
@ThreadSafe
@GoblinManagedBean("RemoteClient")
@GoblinManagedLogger("goblin.remote.client.transport")
class RemoteTransportClientManager private constructor()
  : GoblinManagedObject(), RemoteTransportClientManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteTransportClientManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val buffer = mutableMapOf<RemoteTransportClientId, RemoteTransportClient>()

  fun openConnection(node: String): RemoteTransportClient {
    val map = HttpUtils.parseQueryString(node)
    val serverId = map["serverId"]!!
    val serverHost = map["serverHost"]!!
    val serverPort = map["serverPort"]!!.toInt()
    val serverWeight = map["serverWeight"]!!.toInt()
    val clientId = RemoteTransportClientId(serverId, serverHost, serverPort)
    lock.read {
      buffer[clientId]?.run {
        return this
      }
    }
    lock.write {
      buffer[clientId]?.run {
        return this
      }
      val connection = RemoteTransportClient(this, clientId, serverWeight)
      buffer[clientId] = connection
      connection.transportClient().connectFuture().addListener {
        try {
          it.get()?.run {
            if (this.available()) {
              val serverText = "${this.getServerName()}@${this.getServerHost()}:${this.getServerPort()}"
              logger.info("CONNECTED: {}", serverText)
            }
          }
        } catch (ignore: Exception) {
        }
      }
      return connection
    }
  }

  internal fun closeConnection(clientId: RemoteTransportClientId) {
    lock.write {
      buffer.remove(clientId)?.dispose()
    }
  }
}