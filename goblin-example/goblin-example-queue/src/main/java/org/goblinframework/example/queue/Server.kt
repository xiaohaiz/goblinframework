package org.goblinframework.example.queue

import org.goblinframework.bootstrap.core.StandaloneServer
import org.goblinframework.core.container.GoblinSpringContainer
import org.goblinframework.core.container.SpringContainer
import org.goblinframework.example.queue.producer.SampleProducer

@GoblinSpringContainer("/config/queue.xml")
class Server : StandaloneServer() {

  override fun doService(container: SpringContainer?) {
    val producer = container!!.applicationContext.getBean(SampleProducer::class.java)
  }
}

fun main(args: Array<String>) {
  Server().bootstrap(args)
}