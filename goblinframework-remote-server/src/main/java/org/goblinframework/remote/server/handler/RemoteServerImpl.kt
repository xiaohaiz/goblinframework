package org.goblinframework.remote.server.handler

import org.goblinframework.transport.server.channel.TransportServerManager
import org.goblinframework.transport.server.setting.TransportServerSetting

class RemoteServerImpl internal constructor() {

  companion object {
    private const val SERVER_NAME = "goblin.remote.Server"
  }

  init {
    val setting = TransportServerSetting.builder()
        .name(SERVER_NAME)
        .port(57213)
        .enableDebugMode()
        .applyHandlerSetting {
          it.transportRequestHandler(RemoteServerHandler.INSTANCE)
        }
        .build()
    val server = TransportServerManager.INSTANCE.createTransportServer(setting)
    server.start()
  }

  internal fun close() {
    TransportServerManager.INSTANCE.closeTransportServer(SERVER_NAME)
  }
}