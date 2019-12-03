package org.goblinframework.example.database

import org.goblinframework.bootstrap.core.StandaloneServer
import org.goblinframework.core.container.GoblinSpringContainer

@GoblinSpringContainer("/config/goblin-example-database.xml")
class Server : StandaloneServer() {

}

fun main(args: Array<String>) {
  Server().bootstrap(args)
}