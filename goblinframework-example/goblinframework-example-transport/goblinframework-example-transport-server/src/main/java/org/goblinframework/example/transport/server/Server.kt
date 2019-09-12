package org.goblinframework.example.transport.server

import org.goblinframework.bootstrap.core.StandaloneServer
import org.goblinframework.core.container.SpringContainer
import org.goblinframework.core.container.UseSpringContainer
import org.goblinframework.core.util.SystemUtils
import org.goblinframework.transport.server.channel.TransportServerManager
import org.goblinframework.transport.server.module.TransportServerSetting

@UseSpringContainer("/config/goblinframework-example-transport-server.xml")
class Server : StandaloneServer() {

  override fun doService(container: SpringContainer?) {
    val setting = TransportServerSetting.builder()
        .name("goblinframework-example-transport-server")
        .port(59766)
        .enableDebugMode()
        .applyThreadPoolSetting {
          it.bossThreads(1)
          it.workerThreads(SystemUtils.estimateThreads())
        }
        .build()
    TransportServerManager.INSTANCE.createTransportServer(setting).start()
  }

  override fun doShutdown() {
    TransportServerManager.INSTANCE.closeTransportServer("goblinframework-example-transport-server")
  }
}

fun main(args: Array<String>) {
  Server().bootstrap(args)
}