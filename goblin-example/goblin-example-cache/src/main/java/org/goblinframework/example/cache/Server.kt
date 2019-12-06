package org.goblinframework.example.cache

import org.goblinframework.bootstrap.core.StandaloneServer
import org.goblinframework.core.container.GoblinSpringContainer

@GoblinSpringContainer("/config/cache.xml")
class Server : StandaloneServer()

fun main(args: Array<String>) {
  Server().bootstrap(args)
}