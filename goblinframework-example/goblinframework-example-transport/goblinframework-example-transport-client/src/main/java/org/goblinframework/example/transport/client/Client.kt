package org.goblinframework.example.transport.client

import org.goblinframework.bootstrap.core.StandaloneClient
import org.goblinframework.core.container.SpringContainer
import org.goblinframework.core.container.UseSpringContainer
import org.goblinframework.core.util.NetworkUtils
import org.goblinframework.transport.client.handler.TransportClientManager
import org.goblinframework.transport.client.setting.ClientSetting

@UseSpringContainer("/config/goblinframework-example-transport-client.xml")
class Client : StandaloneClient() {

  override fun doExecute(container: SpringContainer?) {
    val setting = ClientSetting.builder()
        .name("goblinframework-example-transport-client")
        .serverHost(NetworkUtils.getLocalAddress())
        .serverPort(59766)
        .autoReconnect(true)
        .enableDebugMode()
        .build()
    val client = TransportClientManager.INSTANCE.createConnection(setting)
    client.connectFuture().awaitUninterruptibly()
    TransportClientManager.INSTANCE.closeConnection("goblinframework-example-transport-client")
  }
}

fun main(args: Array<String>) {
  Client().bootstrap(args)
}