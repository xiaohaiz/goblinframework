package org.goblinframework.example.remote.client

import org.goblinframework.api.function.Block1
import org.goblinframework.bootstrap.core.StandaloneClient
import org.goblinframework.core.container.SpringContainer
import org.goblinframework.core.util.NetworkUtils
import org.goblinframework.example.remote.api.EchoService
import org.goblinframework.example.remote.api.TimeService
import org.goblinframework.remote.core.protocol.RemoteRequest
import org.goblinframework.remote.core.protocol.RemoteResponse
import org.goblinframework.transport.client.channel.TransportClientManager
import org.goblinframework.transport.client.flight.MessageFlightManager
import org.goblinframework.transport.client.setting.TransportClientSetting

class Client : StandaloneClient() {

  override fun doExecute(container: SpringContainer?) {
    val setting = TransportClientSetting.builder()
        .name("org.goblinframework.example.remote.client.Client")
        .serverHost(NetworkUtils.getLocalAddress())
        .serverPort(57213)
        .enableDebugMode()
        .autoReconnect(true)
        .enableReceiveShutdown()
        .applyHandlerSetting {
          it.enableMessageFlight()
        }
        .build()
    val client = TransportClientManager.INSTANCE.createConnection(setting)
    client.connectFuture().awaitUninterruptibly()
    if (client.available()) {
      var flight = MessageFlightManager.INSTANCE.createMessageFlight(true)
          .prepareRequest(Block1 {
            val request = RemoteRequest()
            request.serviceInterface = EchoService::class.java.name
            request.serviceGroup = "goblin"
            request.serviceVersion = "1.0"
            request.methodName = "echo"
            request.parameterTypes = arrayOf(String::class.java.name)
            request.returnType = String::class.java.name
            request.arguments = arrayOf("HELLO, WORLD!")
            it.writePayload(request)
          })
          .sendRequest(client)
          .uninterruptibly
      var response = flight.responseReader().readPayload() as RemoteResponse
      println("==================================")
      println(response.result)
      println("==================================")


      flight = MessageFlightManager.INSTANCE.createMessageFlight(true)
          .prepareRequest(Block1 {
            val request = RemoteRequest()
            request.serviceInterface = TimeService::class.java.name
            request.serviceGroup = "goblin"
            request.serviceVersion = "1.0"
            request.methodName = "currentTimeMillis"
            request.parameterTypes = emptyArray()
            request.returnType = Long::class.java.name
            request.arguments = emptyArray()
            it.writePayload(request)
          })
          .sendRequest(client)
          .uninterruptibly
      response = flight.responseReader().readPayload() as RemoteResponse
      println("==================================")
      println(response.result)
      println("==================================")
    }
    TransportClientManager.INSTANCE.closeConnection(setting.name())
  }
}

fun main(args: Array<String>) {
  Client().bootstrap(args)
}