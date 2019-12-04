package org.goblinframework.example.database

import org.goblinframework.bootstrap.core.StandaloneServer
import org.goblinframework.core.container.GoblinSpringContainer
import org.goblinframework.core.container.SpringContainer
import org.goblinframework.example.database.service.ExampleService

@GoblinSpringContainer("/config/goblin-example-database.xml")
class Server : StandaloneServer() {
  override fun doService(container: SpringContainer?) {
    val exampleService = container!!.applicationContext.getBean(ExampleService::class.java)
    exampleService.mongoStaticExampleDataService()
//    exampleService.mysqlStaticExampleDataService()
  }
}

fun main(args: Array<String>) {
  Server().bootstrap(args)
}