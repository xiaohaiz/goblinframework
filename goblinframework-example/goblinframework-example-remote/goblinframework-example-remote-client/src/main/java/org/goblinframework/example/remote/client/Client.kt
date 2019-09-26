package org.goblinframework.example.remote.client

import org.goblinframework.api.container.GoblinSpringContainer
import org.goblinframework.bootstrap.core.StandaloneClient
import org.goblinframework.core.container.SpringContainer
import org.goblinframework.core.util.AsciiTable
import org.goblinframework.core.util.ThreadUtils
import org.goblinframework.example.remote.client.service.TimeServiceClient

@GoblinSpringContainer("/config/goblinframework-example-remote-client.xml")
class Client : StandaloneClient() {

  override fun doExecute(container: SpringContainer?) {
//    val setting = TransportClientSetting.builder()
//        .name("org.goblinframework.example.remote.client.Client")
//        .serverHost(NetworkUtils.getLocalAddress())
//        .serverPort(57213)
//        .enableDebugMode()
//        .autoReconnect(true)
//        .enableReceiveShutdown()
//        .applyHandlerSetting {
//          it.enableMessageFlight()
//        }
//        .build()
//    val client = TransportClientManager.INSTANCE.createConnection(setting)
//    client.connectFuture().awaitUninterruptibly()
//    if (client.available()) {
//      var flight = MessageFlightManager.INSTANCE.createMessageFlight(true)
//          .prepareRequest(Block1 {
//            val request = RemoteRequest()
//            request.serviceInterface = EchoService::class.java.name
//            request.serviceGroup = "goblin"
//            request.serviceVersion = "1.0"
//            request.methodName = "echo"
//            request.parameterTypes = arrayOf(String::class.java.name)
//            request.returnType = String::class.java.name
//            request.arguments = arrayOf("HELLO, WORLD!")
//            it.writePayload(request)
//          })
//          .sendRequest(client)
//          .uninterruptibly
//      var response = flight.responseReader().readPayload() as RemoteResponse
//      println("==================================")
//      println(response.result)
//      println("==================================")
//
//
//      flight = MessageFlightManager.INSTANCE.createMessageFlight(true)
//          .prepareRequest(Block1 {
//            val request = RemoteRequest()
//            request.serviceInterface = TimeService::class.java.name
//            request.serviceGroup = "goblin"
//            request.serviceVersion = "1.0"
//            request.methodName = "currentTimeMillis"
//            request.parameterTypes = emptyArray()
//            request.returnType = Long::class.java.name
//            request.arguments = emptyArray()
//            it.writePayload(request)
//          })
//          .sendRequest(client)
//          .uninterruptibly
//      response = flight.responseReader().readPayload() as RemoteResponse
//      println("==================================")
//      println(response.result)
//      println("==================================")
//    }
//    TransportClientManager.INSTANCE.closeConnection(setting.name())

    val tsc = container!!.applicationContext.getBean(TimeServiceClient::class.java)
    val ts = tsc.timeService

    val table = AsciiTable()
    table.columns.add(AsciiTable.Column("Method"))
    table.columns.add(AsciiTable.Column("Result"))
    val row = AsciiTable.Row()
    row.values.add("currentTimeMillis")
    row.values.add(ts.currentTimeMillis().toString())
    table.data.add(row)
    table.calculateColumnWidth()
    println(table.render())

    ThreadUtils.joinCurrentThread()
  }
}

fun main(args: Array<String>) {
  Client().bootstrap(args)
}