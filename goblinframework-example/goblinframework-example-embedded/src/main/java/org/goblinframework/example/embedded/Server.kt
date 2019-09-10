package org.goblinframework.example.embedded

import org.goblinframework.bootstrap.core.StandaloneServer
import org.goblinframework.core.container.SpringContainer
import org.goblinframework.core.container.UseSpringContainer

@UseSpringContainer("/config/goblin-example-embedded.xml")
class Server : StandaloneServer() {

  override fun doService(container: SpringContainer?) {

  }

}

fun main(args: Array<String>) {
  Server().bootstrap(args)
}