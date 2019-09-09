package org.goblinframework.bootstrap

import org.goblinframework.bootstrap.core.StandaloneServer

class Server : StandaloneServer() {

  override fun doService() {
    println("")
  }
}

fun main(args: Array<String>) {
  Server().bootstrap(args)
}