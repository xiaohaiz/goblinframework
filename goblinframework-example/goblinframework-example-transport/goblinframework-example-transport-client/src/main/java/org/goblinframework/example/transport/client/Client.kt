package org.goblinframework.example.transport.client

import org.goblinframework.api.common.Block1
import org.goblinframework.api.container.GoblinSpringContainer
import org.goblinframework.bootstrap.core.StandaloneClient
import org.goblinframework.core.container.SpringContainer
import org.goblinframework.core.util.NetworkUtils
import org.goblinframework.transport.client.channel.TransportClientManager
import org.goblinframework.transport.client.flight.MessageFlightManager
import org.goblinframework.transport.client.setting.TransportClientSetting

@GoblinSpringContainer("/config/goblinframework-example-transport-client.xml")
class Client : StandaloneClient() {

  override fun doExecute(container: SpringContainer?) {
    val setting = TransportClientSetting.builder()
        .name("goblinframework-example-transport-client")
        .serverHost(NetworkUtils.getLocalAddress())
        .serverPort(59766)
        .autoReconnect(true)
        .enableReceiveShutdown()
        //.enableSendHeartbeat()
        .applyHandlerSetting {
          it.enableMessageFlight()
        }
        .enableDebugMode()
        .build()
    val client = TransportClientManager.INSTANCE.createConnection(setting)
    client.connectFuture().awaitUninterruptibly()
    if (client.available()) {
      val flight = MessageFlightManager.INSTANCE.createMessageFlight(true)
          .prepareRequest(Block1 {
            it.writePayload("HELLO, WORLD!")
          })
          .sendRequest(client)
      val reader = flight.uninterruptibly.responseReader()
      val ret = reader.readPayload()
      println(ret)
    }
    Thread.currentThread().join()
  }

  override fun doShutdown() {
    TransportClientManager.INSTANCE.closeConnection("goblinframework-example-transport-client")
  }
}

fun main(args: Array<String>) {
  Client().bootstrap(args)
}