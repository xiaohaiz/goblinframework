package org.goblinframework.remote.server.handler

import org.goblinframework.api.core.GoblinManagedBean
import org.goblinframework.api.core.GoblinManagedObject
import org.goblinframework.core.config.ConfigManager
import org.goblinframework.core.conversion.ConversionUtils
import org.goblinframework.core.util.StopWatch
import org.goblinframework.remote.server.module.config.RemoteServerConfig
import org.goblinframework.transport.server.channel.TransportServer
import org.goblinframework.transport.server.channel.TransportServerManager
import org.goblinframework.transport.server.setting.TransportServerSetting

@GoblinManagedBean(type = "RemoteServer")
class RemoteServer
internal constructor(config: RemoteServerConfig)
  : GoblinManagedObject(), RemoteServerMXBean {

  private val watch = StopWatch()
  private val server: TransportServer

  init {
    val builder = TransportServerSetting.builder()
        .name(config.getName())
        .host(config.getHost())
        .port(config.getPort())
        .applyHandlerSetting {
          it.transportRequestHandler(RemoteServerHandler.INSTANCE)
        }
    val s = ConfigManager.INSTANCE.getConfig("remote", "serverDebugMode")
    if (ConversionUtils.toBoolean(s)) {
      builder.enableDebugMode()
    }
    val setting = builder.build()
    server = TransportServerManager.INSTANCE.createTransportServer(setting)
    server.start()
  }

  override fun disposeBean() {
    TransportServerManager.INSTANCE.closeTransportServer(server.getName())
    watch.stop()
  }

  override fun getTransportServer(): TransportServer {
    return server
  }
}