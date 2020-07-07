package org.goblinframework.example.queue

import org.goblinframework.bootstrap.core.StandaloneServer
import org.goblinframework.core.container.GoblinSpringContainer
import org.goblinframework.core.container.SpringContainer
import org.goblinframework.example.queue.producer.SampleProducer
import org.goblinframework.queue.GoblinMessage

@GoblinSpringContainer("/config/queue.xml")
class Server : StandaloneServer() {

  override fun doService(container: SpringContainer?) {
    val producer = container!!.applicationContext.getBean(SampleProducer::class.java)

    producer.getKafkaProducer().send(GoblinMessage.create().data("test"))

    producer.getKafkaProducer().sendAsync(GoblinMessage.create().data("test")).uninterruptibly

    producer.getKafkaJsonProducer().send(GoblinMessage.create().data("testJson"))

    producer.getKafkaJsonProducer().sendAsync(GoblinMessage.create().data("testJson")).uninterruptibly
  }
}

fun main(args: Array<String>) {
  Server().bootstrap(args)
}