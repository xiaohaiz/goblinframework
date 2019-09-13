package org.goblinframework.example.transport.client

import org.goblinframework.bootstrap.core.StandaloneClient
import org.goblinframework.core.container.SpringContainer
import org.goblinframework.core.container.UseSpringContainer
import org.goblinframework.core.util.NetworkUtils
import org.goblinframework.transport.client.handler.TransportClientManager
import org.goblinframework.transport.client.setting.ClientSetting
import org.goblinframework.transport.core.protocol.TransportRequest

@UseSpringContainer("/config/goblinframework-example-transport-client.xml")
class Client : StandaloneClient() {

  override fun doExecute(container: SpringContainer?) {
    val setting = ClientSetting.builder()
        .name("goblinframework-example-transport-client")
        .serverHost(NetworkUtils.getLocalAddress())
        .serverPort(59766)
        .autoReconnect(true)
        .enableReceiveShutdown()
        //.enableSendHeartbeat()
        .enableDebugMode()
        .build()
    val client = TransportClientManager.INSTANCE.createConnection(setting)
    client.connectFuture().awaitUninterruptibly()
    if (client.available()) {
      val request = TransportRequest()
      request.requestId = 1
      request.requestCreateTime = System.currentTimeMillis()
      request.response = true
      request.hasPayload = true
      request.payload = "HELLO, WORLD!".toByteArray(Charsets.UTF_8)
      client.stateChannel().writeMessage(request)
    }
    Thread.currentThread().join()
    TransportClientManager.INSTANCE.closeConnection("goblinframework-example-transport-client")
  }
}

fun main(args: Array<String>) {
  Client().bootstrap(args)
}