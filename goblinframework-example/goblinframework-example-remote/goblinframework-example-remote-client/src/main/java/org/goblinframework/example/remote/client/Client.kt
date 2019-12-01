package org.goblinframework.example.remote.client

import org.goblinframework.bootstrap.core.StandaloneServer
import org.goblinframework.core.container.GoblinSpringContainer
import org.goblinframework.core.container.SpringContainer
import org.goblinframework.example.remote.api.TimeService
import org.goblinframework.remote.client.invocation.java.RemoteJavaClientFactory

@GoblinSpringContainer("/config/goblinframework-example-remote-client.xml")
class Client : StandaloneServer() {

  override fun doService(container: SpringContainer?) {
    val timeService = RemoteJavaClientFactory.createJavaClient(TimeService::class.java)
    timeService.currentDate()
  }
}

fun main(args: Array<String>) {
  Client().bootstrap(args)
}