package org.goblinframework.example.queue

import org.goblinframework.bootstrap.core.StandaloneServer
import org.goblinframework.core.container.GoblinSpringContainer
import org.goblinframework.core.container.SpringContainer

@GoblinSpringContainer("/config/queue.xml")
class Server : StandaloneServer() {

  override fun doService(container: SpringContainer?) {

  }
}

fun main(args: Array<String>) {
  Server().bootstrap(args)
}