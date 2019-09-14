package org.goblinframework.example.remote.server

import org.goblinframework.bootstrap.core.StandaloneServer

class Server : StandaloneServer() {
}

fun main(args: Array<String>) {
  Server().bootstrap(args)
}