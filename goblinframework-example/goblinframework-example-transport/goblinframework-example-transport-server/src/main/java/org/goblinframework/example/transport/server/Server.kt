package org.goblinframework.example.transport.server

import org.goblinframework.bootstrap.core.StandaloneServer
import org.goblinframework.core.container.SpringContainer
import org.goblinframework.core.container.UseSpringContainer
import org.goblinframework.transport.server.manager.TransportServerManager
import org.goblinframework.transport.server.setting.ServerSetting

@UseSpringContainer("/config/goblinframework-example-transport-server.xml")
class Server : StandaloneServer() {

  override fun doService(container: SpringContainer?) {
    val setting = ServerSetting.builder().name("goblinframework-example-transport-server").build()
    TransportServerManager.INSTANCE.createTransportServer(setting).start()
  }

  override fun doShutdown() {
    TransportServerManager.INSTANCE.closeTransportServer("goblinframework-example-transport-server")
  }
}

fun main(args: Array<String>) {
  Server().bootstrap(args)
}