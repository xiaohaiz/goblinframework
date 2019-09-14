package org.goblinframework.example.remote.server

import org.goblinframework.bootstrap.core.StandaloneServer
import org.goblinframework.core.container.SpringContainer
import org.goblinframework.core.container.UseSpringContainer
import org.goblinframework.example.remote.server.service.EchoServiceImpl
import org.goblinframework.remote.server.expose.ExposeServiceScanner

@UseSpringContainer("/config/goblinframework-example-remote-server.xml")
class Server : StandaloneServer() {

  override fun doService(container: SpringContainer?) {
    ExposeServiceScanner.expose(EchoServiceImpl.INSTANCE)
  }
}

fun main(args: Array<String>) {
  Server().bootstrap(args)
}