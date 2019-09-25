package org.goblinframework.remote.server.handler

import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.remote.server.module.config.RemoteServerConfig
import org.goblinframework.transport.server.channel.TransportServer
import org.goblinframework.transport.server.channel.TransportServerManager
import org.goblinframework.transport.server.setting.TransportServerSetting

@GoblinManagedBean(type = "RemoteServer")
class RemoteServer
internal constructor(config: RemoteServerConfig)
  : GoblinManagedObject(), RemoteServerMXBean {

  private val server: TransportServer

  init {
    val setting = TransportServerSetting.builder()
        .name(config.getName())
        .host(config.getHost())
        .port(config.getPort())
        .enableDebugMode()
        .applyHandlerSetting {
          it.transportRequestHandler(RemoteServerHandler.INSTANCE)
        }
        .build()
    server = TransportServerManager.INSTANCE.createTransportServer(setting)
    server.start()
  }

  override fun disposeBean() {
    TransportServerManager.INSTANCE.closeTransportServer(server.getName())
  }

}