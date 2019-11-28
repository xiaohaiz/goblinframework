package org.goblinframework.example.remote.server

import org.goblinframework.bootstrap.core.StandaloneServer
import org.goblinframework.core.container.GoblinSpringContainer

@GoblinSpringContainer("/config/goblinframework-example-remote-server.xml")
class Server : StandaloneServer()

fun main(args: Array<String>) {
  Server().bootstrap(args)
}