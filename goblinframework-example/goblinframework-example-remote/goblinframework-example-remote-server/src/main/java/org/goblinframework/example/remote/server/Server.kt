package org.goblinframework.example.remote.server

import org.goblinframework.bootstrap.core.StandaloneServer
import org.goblinframework.core.container.GoblinSpringContainer
import org.goblinframework.core.container.SpringContainer
import org.goblinframework.example.remote.server.service.EchoServiceImpl
import org.goblinframework.remote.server.service.RemoteServiceManager

@GoblinSpringContainer("/config/goblinframework-example-remote-server.xml")
class Server : StandaloneServer() {

  override fun doService(container: SpringContainer?) {
    RemoteServiceManager.createRemoteService(EchoServiceImpl.INSTANCE)
  }

}

fun main(args: Array<String>) {
  Server().bootstrap(args)
}