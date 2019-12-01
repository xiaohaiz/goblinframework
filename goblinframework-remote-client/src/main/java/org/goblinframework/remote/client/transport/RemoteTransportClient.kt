package org.goblinframework.remote.client.transport

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.remote.client.module.config.RemoteClientConfigManager
import org.goblinframework.remote.client.service.RemoteServiceClient
import org.goblinframework.transport.client.channel.TransportClient
import org.goblinframework.transport.client.channel.TransportClientManager
import org.goblinframework.transport.client.handler.TransportClientConnectedHandler
import org.goblinframework.transport.client.setting.TransportClientSetting

@GoblinManagedBean("RemoteClient")
@GoblinManagedLogger("goblin.remote.client.transport")
class RemoteTransportClient
internal constructor(private val clientId: RemoteTransportClientId,
                     weight: Int)
  : GoblinManagedObject(), RemoteTransportClientMXBean {

  private val transportClient: TransportClient

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
        .weight(weight)
        .applyHandlerSetting {
          it.enableMessageFlight()
          it.transportClientConnectedHandler(object : TransportClientConnectedHandler {
            override fun handleTransportClientConnected(client: TransportClient) {
              TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
    TODO()
  }

  fun release(client: RemoteServiceClient) {
    TODO()
  }

  override fun dispose() {
    TransportClientManager.INSTANCE.closeConnection(clientId.serverId)
  }
}