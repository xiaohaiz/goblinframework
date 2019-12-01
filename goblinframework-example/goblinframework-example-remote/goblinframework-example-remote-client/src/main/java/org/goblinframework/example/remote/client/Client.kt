package org.goblinframework.example.remote.client

import org.goblinframework.bootstrap.core.StandaloneServer
import org.goblinframework.core.container.GoblinSpringContainer
import org.goblinframework.core.container.SpringContainer
import org.goblinframework.example.remote.api.TimeService
import org.goblinframework.remote.client.service.RemoteServiceClientManager
import org.goblinframework.remote.core.service.RemoteServiceId

@GoblinSpringContainer("/config/goblinframework-example-remote-client.xml")
class Client : StandaloneServer() {

  override fun doService(container: SpringContainer?) {
    val serviceId = RemoteServiceId(TimeService::class.java.name, "1.0.0")
    RemoteServiceClientManager.INSTANCE.getRemoteService(serviceId)
  }
}

fun main(args: Array<String>) {
  Client().bootstrap(args)
}