package org.goblinframework.example.transport.server

import org.goblinframework.api.core.GoblinSpringContainer
import org.goblinframework.bootstrap.core.StandaloneServer
import org.goblinframework.core.container.SpringContainer
import org.goblinframework.core.util.SystemUtils
import org.goblinframework.transport.server.channel.TransportServerManager
import org.goblinframework.transport.server.handler.TransportRequestContext
import org.goblinframework.transport.server.handler.TransportRequestHandler
import org.goblinframework.transport.server.setting.TransportServerSetting

@GoblinSpringContainer("/config/goblinframework-example-transport-server.xml")
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
        .applyHandlerSetting {
          it.transportRequestHandler(object : TransportRequestHandler {
            override fun handleTransportRequest(ctx: TransportRequestContext) {
              val payload = ctx.requestReader.readPayload()
              println(payload)
              ctx.responseWriter.writePayload("ONE WORLD, ONE DREAM.")
              ctx.sendResponse()
            }
          })
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