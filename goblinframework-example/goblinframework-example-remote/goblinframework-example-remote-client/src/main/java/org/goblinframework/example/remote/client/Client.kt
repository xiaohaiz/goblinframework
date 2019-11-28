package org.goblinframework.example.remote.client

import org.goblinframework.bootstrap.core.StandaloneClient
import org.goblinframework.core.container.GoblinSpringContainer
import org.goblinframework.core.container.SpringContainer

@GoblinSpringContainer("/config/goblinframework-example-remote-client.xml")
class Client : StandaloneClient() {

  override fun doExecute(container: SpringContainer?) {
  }

}

fun main(args: Array<String>) {
  Client().bootstrap(args)
}