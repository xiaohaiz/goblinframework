package org.goblinframework.remote.client.transport

import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.util.RandomUtils
import org.goblinframework.remote.client.module.config.RemoteClientConfigManager
import org.goblinframework.remote.client.service.RemoteServiceClient
import org.goblinframework.transport.client.channel.TransportClient
import org.goblinframework.transport.client.channel.TransportClientManager
import org.goblinframework.transport.client.handler.TransportClientConnectedHandler
import org.goblinframework.transport.client.setting.TransportClientSetting
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@ThreadSafe
@GoblinManagedBean("RemoteClient")
@GoblinManagedLogger("goblin.remote.client.transport")
class RemoteTransportClient
internal constructor(private val clientManager: RemoteTransportClientManager,
                     private val clientId: RemoteTransportClientId,
                     private val weight: Int)
  : GoblinManagedObject(), RemoteTransportClientMXBean {

  private val id = RandomUtils.nextObjectId()
  private val transportClient: TransportClient
  private val lock = ReentrantReadWriteLock()
  private val buffer = IdentityHashMap<RemoteServiceClient, RemoteServiceClient>()

  init {
    val clientConfig = RemoteClientConfigManager.INSTANCE.getRemoteClientConfig()
    val setting = TransportClientSetting.builder()
        .name(clientId.serverId)
        .serverId(clientId.serverId)
        .serverHost(clientId.serverHost)
        .serverPort(clientId.serverPort)
        .workerThreads(clientConfig.getWorkerThreads())
        .keepAlive(true)
        .autoReconnect(true)
        .sendHeartbeat(clientConfig.getSendHeartbeat())
        .receiveShutdown(false)
        .applyHandlerSetting {
          it.enableMessageFlight()
          it.transportClientConnectedHandler(object : TransportClientConnectedHandler {
            override fun handleTransportClientConnected(client: TransportClient) {
              lock.read {
                buffer.keys.forEach { c ->
                  c.addConnection(this@RemoteTransportClient)
                }
              }
            }
          })
        }
        .build()
    transportClient = TransportClientManager.INSTANCE.createConnection(setting)
  }

  fun transportClient(): TransportClient {
    return transportClient
  }

  fun retain(client: RemoteServiceClient) {
    lock.write {
      if (buffer[client] != null) {
        // specified client already retained, ignore
        return
      }
      transportClient.retain()
      if (transportClient.available()) {
        client.addConnection(this)
      }
      buffer[client] = client
    }
  }

  fun release(client: RemoteServiceClient) {
    lock.write {
      buffer.remove(client)?.run {
        if (transportClient.release()) {
          clientManager.closeConnection(clientId)
        }
      }
    }
  }

  override fun getId(): String {
    return id
  }

  override fun getWeight(): Int {
    return weight
  }

  override fun dispose() {
    TransportClientManager.INSTANCE.closeConnection(clientId.serverId)
  }
}