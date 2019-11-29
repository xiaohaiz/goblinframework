package org.goblinframework.remote.server.transport

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.remote.server.module.config.RemoteServerConfig
import org.goblinframework.transport.server.channel.TransportServer
import org.goblinframework.transport.server.channel.TransportServerMXBean
import org.goblinframework.transport.server.channel.TransportServerManager
import org.goblinframework.transport.server.setting.TransportServerSetting

@GoblinManagedBean("RemoteServer")
class RemoteTransportServer internal constructor(private val config: RemoteServerConfig)
  : GoblinManagedObject(), RemoteTransportServerMXBean {

  private val transportServer: TransportServer

  init {
    val setting = TransportServerSetting.builder()
        .name(config.getName())
        .host(config.getHost())
        .port(config.getPort())
        .applyThreadPoolSetting {
          it.bossThreads(config.getBossThreads())
          it.workerThreads(config.getWorkerThreads())
        }
        .applyHandlerSetting {
          it.transportRequestHandler(RemoteTransportServerHandler())
        }
        .build()
    transportServer = TransportServerManager.INSTANCE.createTransportServer(setting)
    transportServer.start()
  }

  override fun getTransportServer(): TransportServerMXBean {
    return transportServer
  }

  override fun disposeBean() {
    TransportServerManager.INSTANCE.closeTransportServer(config.getName())
  }
}